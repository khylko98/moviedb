package ua.khylko.moviedb.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.khylko.moviedb.dto.movie.request.AdminAddMovieRequestDTO;
import ua.khylko.moviedb.dto.movie.response.AdminGetMovieResponseDTO;
import ua.khylko.moviedb.dto.user.response.AdminGetUserInfoResponseDTO;
import ua.khylko.moviedb.exception.MovieAlreadyExistException;
import ua.khylko.moviedb.exception.MovieNotFoundException;
import ua.khylko.moviedb.exception.UserNotFoundException;
import ua.khylko.moviedb.model.movie.Movie;
import ua.khylko.moviedb.model.user.User;
import ua.khylko.moviedb.service.movie.impl.MovieServiceImpl;
import ua.khylko.moviedb.service.user.UserService;
import ua.khylko.moviedb.service.user.impl.UserServiceImpl;
import ua.khylko.moviedb.utils.validator.movie.AdminAddMovieValidator;

import javax.validation.Valid;

import static ua.khylko.moviedb.exception.manager.ExceptionManager.returnMessageForException;

@RestController
@RequestMapping("/moviedb/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MovieServiceImpl movieService;
    private final AdminAddMovieValidator adminMovieValidator;

    @Autowired
    public AdminController(UserServiceImpl userService,
                           ModelMapper modelMapper,
                           MovieServiceImpl movieService,
                           AdminAddMovieValidator adminMovieValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.movieService = movieService;
        this.adminMovieValidator = adminMovieValidator;
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminGetUserInfoResponseDTO userInfo(@PathVariable String username) {
        log.info("Admin try to get full info about user with username: {}", username);
        return userService
                .findByUsername(username)
                .map(this::convertToAdminGetUserInfoResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("username - User with this username not found"));
    }

    @GetMapping("/movie/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminGetMovieResponseDTO movieInfo(@PathVariable String title) {
        log.info("Admin try to get movie with title: {}", title);
        return movieService
                .findByTitle(title)
                .map(this::convertToAdminGetMovieResponseDTO)
                .orElseThrow(() -> new MovieNotFoundException("title - Movie with this title not found"));
    }

    @PostMapping("/movie/addMovie")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> addMovie(
            @RequestBody @Valid AdminAddMovieRequestDTO movieDTO, BindingResult bindingResult) {
        Movie movie = convertToMovie(movieDTO);
        adminMovieValidator.validate(movie, bindingResult);
        if (bindingResult.hasErrors())
            throw new MovieAlreadyExistException(returnMessageForException(bindingResult));
        movieService.save(movie);
        log.info("Admin save movie with title: {}", movieDTO.getTitle());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private AdminGetUserInfoResponseDTO convertToAdminGetUserInfoResponseDTO(User user) {
        return modelMapper.map(user, AdminGetUserInfoResponseDTO.class);
    }

    private AdminGetMovieResponseDTO convertToAdminGetMovieResponseDTO(Movie movie) {
        return modelMapper.map(movie, AdminGetMovieResponseDTO.class);
    }

    private Movie convertToMovie(AdminAddMovieRequestDTO movieDTO) {
        return modelMapper.map(movieDTO, Movie.class);
    }
}
