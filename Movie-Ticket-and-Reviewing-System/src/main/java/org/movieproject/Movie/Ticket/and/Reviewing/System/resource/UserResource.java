package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.movieproject.Movie.Ticket.and.Reviewing.System.enums.Role;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class UserResource {

    private long id;

    @NotBlank(message = "User name is Mandatory")
    private String name;

    @NotBlank(message = "password is mandatory")
    private String password;

    private Role role;

    @NotBlank(message = "Mobile is Mandatory")
    private String mobile;

    @NotBlank(message = "Email is Mandatory")
    private String email;

    private List<TicketResource> tickets;
}
