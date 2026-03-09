package org.movieproject.Movie.Ticket.and.Reviewing.System.exception;

public class NotFoundException extends RuntimeException {

    public final String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
