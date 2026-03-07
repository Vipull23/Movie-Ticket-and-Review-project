package org.movieproject.Movie.Ticket.and.Reviewing.System.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ShowResource {

    private Long id;

    @NotNull(message = "Show Time is Mandatory")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime showTime;

    @NotNull(message = "Movie is Mandatory for Show")
    private Long movieId;

    @NotNull(message = "Theater is Mandatory for Show")
    private Long theaterId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private MovieResource movieResource;

    private TheaterResource theaterResource;

    private List<ShowSeatsResource> seats;
}
