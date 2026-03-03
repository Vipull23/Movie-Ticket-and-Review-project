package org.movieproject.Movie.Ticket.and.Reviewing.System.controller;

import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.ShowResource;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class TheaterController {

    @Autowired
    private ShowService showService;

    @GetMapping("/search")
    public ResponseEntity<List<ShowResource>> search(
            @RequestParam(name = "city", required = true) String cityName,
            @RequestParam(name = "movieName", required = false) String movieName) {
        return ResponseEntity.ok(showService.searchShows(movieName, cityName));
    }

    @PostMapping("/add")
    public ResponseEntity<ShowResource> addShow(@RequestBody ShowResource showResource) {
        showService.addShow(showResource);
        return ResponseEntity.ok(showResource);
    }
}
