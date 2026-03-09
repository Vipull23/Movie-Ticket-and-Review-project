package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import lombok.*;
import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.SeatType;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ShowSeatsResource {
    private long id;

    private String seatNumber;

    private int rate;

    private SeatType seatType;

    private boolean booked;

    private LocalDateTime bookedAt;
}
