package org.movieproject.Movie.Ticket.and.Reviewing.System.service.response;

import lombok.*;
import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.Genre;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

    private String title;
    private Genre genre;
    private Double rating;
    private List<ReviewResponse> reviews;
}

