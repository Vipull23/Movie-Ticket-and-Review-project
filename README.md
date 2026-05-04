# MovieNow — Movie Ticket Booking & Reviewing System

A full-stack Spring Boot backend that powers a cinema booking platform end to end. A user signs up, finds a show playing in their city, selects seats, and books a ticket — all in one API flow. The moment a ticket is confirmed, the system publishes a Kafka event, and a notification consumer picks it up and dispatches an email confirmation to the user. Separately, any user can leave a review for a movie and the platform recalculates and stores the movie's aggregate rating automatically.

---

## Table of Contents

1. [What this project covers](#what-this-project-covers)
2. [The full booking flow](#the-full-booking-flow)
3. [Architecture](#architecture)
4. [Core problems this system solves](#core-problems-this-system-solves)
5. [Module breakdown](#module-breakdown)
6. [API reference](#api-reference)
7. [Technology stack](#technology-stack)

---

## What this project covers

The system is built around four main actors — **Admin**, **Theater**, **User**, and **Movie** — and ties them together through a booking and review lifecycle:

- An **Admin** adds movies to the platform.
- A **Theater Manager** registers a theater (city, address) and creates shows by linking a movie to a theater at a specific time. The system auto-generates seat records for every show.
- A **User** signs up, searches for shows by city/movie/theater, picks seats, and books. The backend validates seat availability, calculates the total amount, writes the ticket, and fires a Kafka message.
- A **Notification Consumer** listens on the Kafka topic and sends an email (and logs an SMS stub) to the user.
- A **Reviewer** submits a star rating and text review for a movie. The platform computes the new average rating and persists it back to the movie record in real time.

Everything is secured with Spring Security — admin-only endpoints are locked behind `ADMIN` authority, user endpoints behind `USER` authority, and public endpoints (movie search, show search) are open.

---

## The full booking flow

### Step 1 — Admin adds a movie

`POST /admin/movie/add` with a title and genre. The movie lands in `movie_table` with an initial rating of `0.0`.

### Step 2 — Theater is registered

`POST /theater/add` (TheaterService) creates the theater entity and immediately seeds it with 10 seats — 5 `REGULAR` (row 1A–1E) and 5 `RECLINER` (row 2A–2E) — saved to `theater_seats`.

### Step 3 — Show is created

`POST /show/add` with a movieId, theaterId, and showTime.

**What happens inside `ShowService.addShow()`:**
1. Validates the movie and theater both exist — throws `NotFoundException` if either is missing.
2. Builds a `Show` entity and links the movie and theater.
3. Calls `generateShowSeats()` — iterates over the theater's seat blueprint and creates a fresh `ShowSeat` record for each one, all marked `booked = false`. These are saved to `show_seats`. Every show starts with a clean inventory of its own seat copies.
4. Saves the show and returns the full `ShowResource`.

### Step 4 — User searches for shows

`GET /show/search?city=Mumbai&movieName=Inception`

`ShowService.searchShows()` dispatches to one of three native JPA queries depending on which filters are provided — by movie name + city, by theater name + city, or by city alone. Returns a list of matching shows with their available seats embedded.

### Step 5 — User books a ticket

`POST /ticket/book` with userId, showId, seatType, and a set of seat numbers.

**What happens inside `TicketService.bookTicket()`:**
1. Validates user and show exist.
2. Filters the show's `ShowSeat` list to find seats that match the requested type, are not yet booked, and are in the requested set.
3. If the count of matching seats doesn't equal the count of requested seats, throws `NotFoundException("Seats Not Available")`.
4. Builds a `Ticket`, marks each seat as `booked = true` with a `bookedAt` timestamp, accumulates the total `amount`, and links the seat back to the ticket.
5. Saves the ticket (cascade saves all seat updates).
6. Publishes a `TicketMessage` to the `TICKET_BOOKED` Kafka topic as a JSON string.
7. Returns the `TicketResource` to the caller.

### Step 6 — Notification fires asynchronously

`NotificationConsumer` is a `@KafkaListener` on topic `TICKET_BOOKED`. It deserializes the message and hands it to `NotificationService`, which composes a `SimpleMailMessage` and sends it via the configured JavaMailSender (Gmail SMTP). An SMS stub is also logged (ready for a real SMS gateway integration).

### Step 7 — User submits a review

`POST /review/add` with a movieId, text review, and star rating.

`ReviewService.addReview()` saves the review, then immediately calls `reviewRepository.getReviewAverage(movieId)` — a native SQL `avg(rating)` query — and writes the new average back to `movie.rating`. The next time someone fetches the movie, they see a live-calculated score.

---

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                      HTTP Layer                         │
│  AdminController  MovieController  ShowController       │
│  TheaterController  TicketController  UserController    │
│  ReviewController                                       │
└──────────────────────────┬──────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│                    Service Layer                         │
│  AdminService  MovieService  ShowService  TheaterService │
│  TicketService  UserService  ReviewService               │
│  NotificationService  UserAuthService                    │
└────────────┬───────────────────────────┬────────────────┘
             │                           │
┌────────────▼────────┐   ┌─────────────▼──────────────┐
│   Repository Layer  │   │      Kafka Pipeline         │
│  (Spring Data JPA)  │   │                             │
│                     │   │  TicketService               │
│  MovieRepository    │   │    └─▶ KafkaTemplate.send() │
│  TheaterRepository  │   │         (TICKET_BOOKED)      │
│  ShowRepository     │   │                             │
│  ShowSeatsRepo      │   │  NotificationConsumer        │
│  TicketRepository   │   │    └─▶ @KafkaListener        │
│  UserRepository     │   │         └─▶ NotificationSvc  │
│  ReviewRepository   │   │              └─▶ Email + SMS  │
└────────────┬────────┘   └────────────────────────────┘
             │
┌────────────▼────────┐
│      MySQL DB       │
│  movie_table        │
│  theaters           │
│  theater_seats      │
│  shows              │
│  show_seats         │
│  tickets            │
│  users              │
│  review_table       │
└─────────────────────┘
```

---

## Core problems this system solves

### Seat inventory consistency

ShowSeats are not shared between shows. When a show is created, `generateShowSeats()` clones the theater's seat blueprint into a fresh set of `ShowSeat` rows bound to that specific show. Booking marks individual `ShowSeat` rows as `booked=true` — so concurrent bookings for overlapping seats on the same show are governed at the row level.

The booking filter in `TicketService` matches requested seats against the show's seat list using three simultaneous conditions: correct seat type, not already booked, and seat number in the requested set. If even one requested seat is unavailable, the entire booking is rejected before any write happens.

### Asynchronous notification via Kafka

The booking HTTP response does not wait for email delivery. Once the ticket is saved, a Kafka message is published and the API returns immediately. The `NotificationConsumer` processes delivery on its own thread/group (`ticketGroup`), meaning email failures never surface back to the user as a booking failure and cannot slow down the booking endpoint.

### Live movie rating

Rather than computing an average at read time across potentially thousands of reviews, the system materializes the average into `movie.rating` on every write. `reviewRepository.getReviewAverage()` runs a single `AVG(rating)` SQL query scoped to the movie's ID, and the result is persisted back to the movie row. Read performance on movie lookups is O(1) regardless of review volume.

### Role-based access control

`SecurityConfiguration` wires three tiers of access using Spring Security's `authorizeHttpRequests`:
- `/admin/**` → requires `ADMIN` authority
- `/user/**` → requires `USER` authority
- Everything else → open (movie search, show search, ticket booking are intentionally public for demo purposes)

`MyAuthorityProvider` implements a custom `AuthenticationProvider` that loads the user from the database via `UserAuthService`, verifies the password via `PasswordEncoder`, and returns a fully-populated `UsernamePasswordAuthenticationToken` with the user's authorities — the authorities Spring Security then uses for every downstream `requestMatchers` check.

---

## Module breakdown

### Domain (`domain/`)

The JPA entities that map directly to database tables.

| Entity | Table | Key relationships |
|---|---|---|
| `Movie` | `movie_table` | One-to-many → `Review` |
| `Theater` | `theaters` | One-to-many → `Show`, `TheaterSeats` |
| `TheaterSeats` | `theater_seats` | Many-to-one → `Theater` |
| `Show` | `shows` | Many-to-one → `Movie`, `Theater`; One-to-many → `ShowSeat`, `Ticket` |
| `ShowSeat` | `show_seats` | Many-to-one → `Show`, `Ticket` |
| `Ticket` | `tickets` | Many-to-one → `User`, `Show`; One-to-many → `ShowSeat` |
| `User` | `users` | One-to-many → `Ticket`; implements `UserDetails` |
| `Review` | `review_table` | Many-to-one → `Movie` |

Each entity owns its own `toEntity()` and `toResource()` static conversion methods, keeping the domain layer self-contained and avoiding any service-layer mapping boilerplate.

### Resources (`resource/`)

Plain DTOs for request/response serialization — `ShowResource`, `TicketResource`, `BookingResource`, `UserResource`, `TheaterResource`, `ShowSeatsResource`, `MovieResource`, `ReviewResource`, and `TicketMessage` (the Kafka payload shape).

### Services (`service/`)

| Service | Responsibility |
|---|---|
| `AdminService` | Delegates movie persistence |
| `MovieService` | Find by title or genre; genre list is sorted by rating descending and capped at the top 5 |
| `ShowService` | Create shows with auto-generated seats; multi-filter show search |
| `TheaterService` | Create theater with seeded seat layout |
| `TicketService` | Seat availability check, ticket write, Kafka publish |
| `ReviewService` | Review save + live rating recalculation |
| `UserService` | Sign up (deduplication by mobile) and profile fetch |
| `UserAuthService` | `UserDetailsService` implementation for Spring Security |
| `NotificationService` | Email dispatch via JavaMailSender; SMS stub |

### Repositories (`repository/`)

All extend `JpaRepository`. Notable custom queries:

- `ShowRepository` — three native SQL joins across `shows`, `movies`, and `theaters` for flexible show search.
- `ReviewRepository` — native `AVG(rating)` query scoped by `movie_id`.

### Configuration (`config/`)

- `SecurityConfiguration` — `SecurityFilterChain`, `PasswordEncoder` bean.
- `MyAuthorityProvider` — custom `AuthenticationProvider`.
- `KafkaConfig` — producer and consumer factory beans wired to `localhost:9092`.
- `MailConfiguration` — `JavaMailSenderImpl` and `ObjectMapper` beans.

### Consumer (`consumer/`)

`NotificationConsumer` — `@KafkaListener` on `TICKET_BOOKED`. Deserializes the JSON string into a `TicketMessage` using Jackson and hands off to `NotificationService`.

---

## API reference

| Method | Path | Authority | What it does |
|---|---|---|---|
| POST | `/admin/movie/add` | ADMIN | Add a new movie |
| POST | `/user/signup` | open | Register a new user |
| GET | `/user/{id}` | USER | Fetch user profile + ticket history |
| GET | `/movie/title?title=` | open | Find a movie by exact title |
| GET | `/movie/genre?genre=` | open | Top movies by genre (sorted by rating) |
| POST | `/show/add` | open | Create a new show |
| GET | `/show/search?city=&movieName=&theaterName=` | open | Search shows with flexible filters |
| POST | `/ticket/book` | open | Book a ticket (seat validation + Kafka publish) |
| GET | `/ticket/{id}` | open | Fetch a booked ticket |
| POST | `/review/add` | open | Submit a review (triggers rating recalculation) |
| GET | `/review/find?reviewId=` | open | Fetch a single review |

### Sample: Book a ticket

```http
POST /ticket/book
Content-Type: application/json

{
  "userId": 1,
  "showId": 3,
  "seatType": "REGULAR",
  "seatsNumbers": ["1A", "1B"]
}
```

Response:
```json
{
  "id": 12,
  "allottedSeats": "1A 1B ",
  "amount": 200.0,
  "bookedAt": "2025-05-03T14:30:00",
  "show": null
}
```

### Sample: Submit a review

```http
POST /review/add
Content-Type: application/json

{
  "movieId": 5,
  "movieReview": "Brilliant screenplay, loved every frame.",
  "rating": 4.7
}
```

The movie's aggregate rating is updated in the same transaction.

---

## Technology stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.x |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Messaging | Apache Kafka (`spring-kafka`) |
| Security | Spring Security (HTTP Basic, custom AuthProvider) |
| Email | Spring Mail (JavaMailSender, Gmail SMTP) |
| Serialization | Jackson (jackson-databind, jackson-datatype-jsr310) |
| Validation | Spring Validation (Jakarta Bean Validation) |
| Boilerplate reduction | Lombok |
| Build | Maven (via Maven Wrapper — no install required) |
| Language | Java 17 |
