package org.movieproject.Movie.Ticket.and.Reviewing.System.repository;

import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {

}
