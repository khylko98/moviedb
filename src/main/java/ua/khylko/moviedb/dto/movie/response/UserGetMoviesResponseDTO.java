package ua.khylko.moviedb.dto.movie.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.khylko.moviedb.model.movie.Movie;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserGetMoviesResponseDTO {
    private List<Movie> watchedMovies;
}
