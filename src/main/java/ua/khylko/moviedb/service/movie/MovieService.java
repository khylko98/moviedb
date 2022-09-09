package ua.khylko.moviedb.service.movie;

import ua.khylko.moviedb.model.movie.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Optional<Movie> findByTitle(String title);
    List<Movie> getMovies(String username);
    void addMovie(String username, String title);
    void save(Movie movie);
    void saveDirector(Movie movie);
    void saveActors(Movie movie);
}
