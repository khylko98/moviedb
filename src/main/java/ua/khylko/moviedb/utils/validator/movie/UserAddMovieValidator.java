package ua.khylko.moviedb.utils.validator.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.khylko.moviedb.dto.movie.request.UserAddMovieRequestDTO;
import ua.khylko.moviedb.service.movie.MovieService;
import ua.khylko.moviedb.service.movie.impl.MovieServiceImpl;

@Component
public class UserAddMovieValidator implements Validator {
    private final MovieService movieService;

    @Autowired
    public UserAddMovieValidator(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserAddMovieRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserAddMovieRequestDTO movie = (UserAddMovieRequestDTO) target;
        if (movieService.findByTitle(movie.getTitle()).isEmpty())
            errors.rejectValue( "title",
                            "",
                        "Movie with title not found on site database");
    }
}
