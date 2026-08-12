package org.movieproject.Movie.Ticket.and.Reviewing.System.controller;

import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.TheaterResource;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping("/add")
    public ResponseEntity<TheaterResource> addTheater(@RequestBody TheaterResource theaterResource) {
        return ResponseEntity.ok(theaterService.addTheater(theaterResource));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResource> getTheater(@PathVariable long id) {
        return ResponseEntity.ok(theaterService.getTheater(id));
    }
}