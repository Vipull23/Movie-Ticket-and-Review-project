package org.movieproject.Movie.Ticket.and.Reviewing.System.service;

import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.Genre;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.MovieRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.response.MovieResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieResponse findMovie(String title) {
        Movie movie = movieRepository.findByTitle(title);
        if (Objects.nonNull(movie)) {
            return movie.toMovieResponse();
        }
        return null;
    }

    public List<MovieResponse> findMoviesByGenre(String genre) {
        if (Arrays.stream(Genre.values()).noneMatch(g -> g.toString().equals(genre)))
            return new ArrayList<>();
        // implement caching logic here for future
        List<Movie> movieList = movieRepository.findByGenre(Genre.valueOf(genre));
        if (!CollectionUtils.isEmpty(movieList)) {
            List<MovieResponse> movieResponseList = movieList.stream().sorted(Comparator.comparing(Movie::getRating, Comparator.reverseOrder())).map(m -> m.toMovieResponse()).collect(Collectors.toList());
            if (movieResponseList.size() > 5) {
                return movieResponseList.subList(0, 4);
            }else{
                return movieResponseList;}
            }
        return new ArrayList<>();
    }
}
