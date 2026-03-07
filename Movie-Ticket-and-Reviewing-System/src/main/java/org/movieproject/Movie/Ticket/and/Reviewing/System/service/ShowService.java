package org.movieproject.Movie.Ticket.and.Reviewing.System.service;


import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Movie;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Show;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.MovieRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.ShowRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.ShowSeatsRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.TheaterRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.ShowResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowService {

    Logger log = LoggerFactory.getLogger(ShowService.class);

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowSeatsRepository showSeatsRepository;

    public ShowService() {
    }


    public ShowResource addShow(ShowResource showResource) {

        Optional<Movie> optionalMovie = movieRepository.findById(showResource.getMovieId());

//        if (!optionalMovie.isPresent()) {
//            throw new NotFoundException("Movie Not Found with ID: " + showResource.getMovieId() + " to add New Movie");
//        }

        Movie movie = movieRepository.findById(showResource.getMovieId())
                .orElseThrow(() ->
                        new ChangeSetPersister.NotFoundException("Movie Not Found with ID: " + showResource.getMovieId() + " to add New Show"));

        Optional<Theater> optionalTheater = theaterRepository.findById(showResource.getTheaterId());

        if (!optionalTheater.isPresent()) {
            throw new ChangeSetPersister.NotFoundException("Theater Not Found with the ID: " + showResource.getTheaterId() + " to add");
        }

        log.info("Adding New Show: " + showResource);

        Show show = Show.toEntity(showResource);

        show.setMovie(optionalMovie.get());
        show.setTheater(optionalTheater.get());
        show.setSeats(generateShowSeats(show.getTheater().getSeats(), show));

        for (ShowSeat seatsEntity : show.getSeats()) {
            seatsEntity.setShow(show);
        }

        show = showRespository.save(show);

        return Show.toResource(show);
    }

    private List<ShowSeat> generateShowSeats(List<TheaterSeats> theaterSeatsEntities, Show show) {

        List<ShowSeat> showSeatsEntities = new ArrayList<>();

        for (TheaterSeats theaterSeats : theaterSeatsEntities) {

            ShowSeat showSeat =
                    ShowSeat.builder()
                            .seatNumber(theaterSeats.getSeatNumber())
                            .seatType(theaterSeats.getSeatType())
                            .rate(100)
                            .build();

            showSeatsEntities.add(showSeat);
        }

        return showSeatsRespository.saveAll(showSeatsEntities);
    }

    public List<ShowResource> searchShows(String movieName, String cityName, String theaterName) {

        if (!StringUtils.hasText(cityName))
            new ArrayList<>();
        List<Show> shows = new ArrayList<>();
        if (StringUtils.hasText(movieName))
            shows = showsRespository.findByMovieNameAndCity(movieName, cityName);
        else if (StringUtils.hasText(theaterName)) {
            shows = showRespository.findByTheaterAndCity(theaterName, cityName);
        } else {
            shows = showRespository.findByCity(cityName);
        }

        if (CollectionUtils.isEmpty(shows))
            return new ArrayList<>();
        else
            return shows.stream().map(Show::toResource).collect(Collectors.toList());
    }


}
