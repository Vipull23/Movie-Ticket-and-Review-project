package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import lombok.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.SeatType;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class BookingResource {
    @NotEmpty(message = "SeatNumbers cannot be empty")
    private Set<String> seatsNumbers;

    @Min(value = 1, message = "Invalid user ID")
    private long userId;

    @Min(value = 1, message = "Invalid show ID")
    private long showId;

    @NotNull(message = "seatType cannot be null")
    private SeatType seatType;

}
