package org.movieproject.Movie.Ticket.and.Reviewing.System.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.Genre;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest {

    private String title;

    private Genre genre;

    public Movie toMovie() {
        return Movie.builder().title(title).genre(genre).rating(0.0).build();
    }

}
