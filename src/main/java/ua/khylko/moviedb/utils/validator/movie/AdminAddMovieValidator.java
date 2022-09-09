package ua.khylko.moviedb.utils.validator.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.khylko.moviedb.model.movie.Movie;
import ua.khylko.moviedb.service.movie.MovieService;
import ua.khylko.moviedb.service.movie.impl.MovieServiceImpl;

@Component
public class AdminAddMovieValidator implements Validator {
    private final MovieService movieService;

    @Autowired
    public AdminAddMovieValidator(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Movie.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Movie movie = (Movie) target;
        if (movieService.findByTitle(movie.getTitle()).isPresent())
            errors.rejectValue( "title",
                            "",
                        "Movie with title already exist");
    }
}
