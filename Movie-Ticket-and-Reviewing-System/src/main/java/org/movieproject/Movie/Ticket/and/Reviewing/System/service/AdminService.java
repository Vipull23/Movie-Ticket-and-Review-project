package org.movieproject.Movie.Ticket.and.Reviewing.System.service;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.MovieRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private MovieRepository movieRepository;

    public AdminService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}
