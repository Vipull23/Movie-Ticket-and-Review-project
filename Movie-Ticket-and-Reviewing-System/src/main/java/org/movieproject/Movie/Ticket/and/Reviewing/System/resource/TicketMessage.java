package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Show;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.ShowSeat;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketMessage {
    private String userName;
    private String mobile;
    private String email;
    private Show show;
    private List<ShowSeat> seats;
}
