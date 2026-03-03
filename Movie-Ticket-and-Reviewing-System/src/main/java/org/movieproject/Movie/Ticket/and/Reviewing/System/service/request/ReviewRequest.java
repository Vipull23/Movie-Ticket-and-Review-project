package org.movieproject.Movie.Ticket.and.Reviewing.System.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Review;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    private String movieReview;

    private double rating;

    private Long movieId;

    public Review toReview() {
        return Review.builder().movieReview(movieReview).rating(rating).movie(Movie.builder().id(movieId).build()).build();

    }
}
