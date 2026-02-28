package org.movieproject.Movie.Ticket.and.Reviewing.System.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.response.ReviewResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "review_table")
//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Review {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String movieReview;

    private double rating;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnore
    private Movie movie; //it will add foreign key in mysql table with <TABLE_NAME>_<ID_NAME> --> //movie

    @CreationTimestamp
    private Date updatedDate;

    @UpdateTimestamp
    private Date updateDate;

    public static ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder().review(review.movieReview).rating(review.rating).build();
    }

    public static List<ReviewResponse> toReviewResponse(List<Review> reviewList) {
        if (Objects.isNull(reviewList)) {
            return new ArrayList<>();
        } else {
            return reviewList.stream().map(Review::toReviewResponse).collect(Collectors.toList());
        }
    }
}
