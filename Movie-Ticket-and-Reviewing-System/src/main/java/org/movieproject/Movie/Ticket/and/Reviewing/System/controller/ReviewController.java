package org.movieproject.Movie.Ticket.and.Reviewing.System.controller;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Review;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.ReviewService;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.request.ReviewRequest;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

//    @PostMapping("/review/add")
    @PostMapping("/add")
    public void addReview(@RequestBody ReviewRequest reviewRequest) {
        reviewService.addReview(reviewRequest.toReview());
    }

//    @GetMapping("/review/find")
    @GetMapping("/find")
    public ReviewResponse getReview(@RequestParam Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }
}
