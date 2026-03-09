package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class TicketResource {
    private long id;

    private String allottedSeats;

    private double amount;

    private LocalDateTime bookedAt;

    private ShowResource show;
}
