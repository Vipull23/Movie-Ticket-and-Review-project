package org.movieproject.Movie.Ticket.and.Reviewing.System.repository;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Genre;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    public Movie findByTitle(String title);

    public List<Movie> findByGenre(Genre genre);
}
