package ua.khylko.moviedb.dto.movie.response;

import lombok.Getter;
import lombok.Setter;
import ua.khylko.moviedb.model.movie.Actor;
import ua.khylko.moviedb.model.movie.Director;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AdminGetMovieResponseDTO {
    private Long id;
    private String title;
    private Date releaseDate;
    private Director director;
    private List<Actor> actors;
}
