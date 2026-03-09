package org.movieproject.Movie.Ticket.and.Reviewing.System.controller;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.movieproject.Movie.Ticket.and.Reviewing.System.service.UserService;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResource> addUser(@RequestBody UserResource userResource) {
        return ResponseEntity.ok(userService.addUser(userResource));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable(name = "id") @Min(value = 1, message = "User cannot be negative") long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
