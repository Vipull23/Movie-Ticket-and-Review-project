package org.movieproject.Movie.Ticket.and.Reviewing.System.repository;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.TheaterSeats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterSeatsRepository extends JpaRepository<TheaterSeats, Long> {
}
