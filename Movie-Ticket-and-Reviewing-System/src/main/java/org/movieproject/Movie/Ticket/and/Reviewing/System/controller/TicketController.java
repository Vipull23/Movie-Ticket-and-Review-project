package org.movieproject.Movie.Ticket.and.Reviewing.System.controller;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.BookingResource;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.TicketResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("book")
    public ResponseEntity<TicketResource> bookTicket(@RequestBody BookingResource bookingResource) {
        log.info("Received Request to book ticket: " + bookingResource);

        return ResponseEntity.ok(ticketService.bookTicket(bookingResource));
    }

    @GetMapping("{id}")
    public ResponseEntity<TicketResource> getTicket(@PathVariable(name = "id") @Min(value = 1, message = "Ticket Id cannot be negative") long id) {
        log.info("Received Request to get ticket: " + id);
        return ResponseEntity.ok(ticketService.getTicket(id));
    }
}
