package org.movieproject.Movie.Ticket.and.Reviewing.System.repository;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowSeatsRepository extends JpaRepository<ShowSeat, Long> {

}
