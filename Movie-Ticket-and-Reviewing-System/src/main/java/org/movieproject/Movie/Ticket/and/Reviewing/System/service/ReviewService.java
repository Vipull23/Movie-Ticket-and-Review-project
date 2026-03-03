package org.movieproject.Movie.Ticket.and.Reviewing.System.service;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Review;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.MovieRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.ReviewRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    public void addReview(Review review) {
        Movie movie = movieRepository.findById(review.getMovie().getId()).orElse(null);
        reviewRepository.save(review);

        if (movie != null) {
            Double average = reviewRepository.getReviewAverage(movie.getId());
            movie.setRating(average);
            movieRepository.save(movie);
        }
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(Review::toReviewResponse).orElse(null);
    }
}
