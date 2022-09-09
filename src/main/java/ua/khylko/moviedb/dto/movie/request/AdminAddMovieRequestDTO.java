package ua.khylko.moviedb.dto.movie.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ua.khylko.moviedb.model.movie.Actor;
import ua.khylko.moviedb.model.movie.Director;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AdminAddMovieRequestDTO {
    @NotBlank(message = "Title can't be empty")
    @Size(min = 1, max = 100,
            message = "Title can be 1-100 characters long")
    private String title;

    @NotNull(message = "Release date can't be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @NotNull(message = "Director can't be empty")
    private Director director;

    @NotNull(message = "Actors can't be empty")
    private List<Actor> actors;
}
