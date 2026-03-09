package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.Genre;

import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class MovieResource {
    private long id;

    private String title;

    private Genre genre;

    private List<ReviewResource> reviews;
}
