package org.movieproject.Movie.Ticket.and.Reviewing.System.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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


}
