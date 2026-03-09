package org.movieproject.Movie.Ticket.and.Reviewing.System.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.movieproject.Movie.Ticket.and.Reviewing.System.domain.User;
import org.movieproject.Movie.Ticket.and.Reviewing.System.repository.UserRepository;
import org.movieproject.Movie.Ticket.and.Reviewing.System.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResource addUser(UserResource userResource) {
        if (userRepository.existsByMobile(userResource.getMobile())) {
            return userResource;
        }

        User user = User.toEntity(userResource);

        user = userRepository.save(user);

        log.info("Added New User" + user.toString());

        return User.toResource(user);
    }

    public UserResource getUser(long id) {
        Optional<User> userEntity = userRepository.findById(id);

        if (userEntity.isEmpty()) {
            log.error("User not found for id: " + id);
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        return User.toResource(userEntity.get());
    }
}
