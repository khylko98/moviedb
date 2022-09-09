package ua.khylko.moviedb.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.khylko.moviedb.dto.movie.request.UserAddMovieRequestDTO;
import ua.khylko.moviedb.dto.movie.response.UserGetMoviesResponseDTO;
import ua.khylko.moviedb.dto.user.request.UserUpdateRequestDTO;
import ua.khylko.moviedb.dto.user.response.UserGetInfoResponseDTO;
import ua.khylko.moviedb.exception.MovieNotFoundException;
import ua.khylko.moviedb.exception.UpdateUserException;
import ua.khylko.moviedb.model.user.User;
import ua.khylko.moviedb.security.UserDetailsImpl;
import ua.khylko.moviedb.service.movie.impl.MovieServiceImpl;
import ua.khylko.moviedb.service.user.UserService;
import ua.khylko.moviedb.service.user.impl.UserServiceImpl;
import ua.khylko.moviedb.utils.validator.movie.UserAddMovieValidator;
import ua.khylko.moviedb.utils.validator.user.UpdateValidator;

import javax.validation.Valid;

import static ua.khylko.moviedb.exception.manager.ExceptionManager.returnMessageForException;

@RestController
@RequestMapping("/moviedb/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MovieServiceImpl movieService;
    private final UpdateValidator updateValidator;
    private final UserAddMovieValidator userMovieValidator;

    @Autowired
    public UserController(UserServiceImpl userService,
                          ModelMapper modelMapper,
                          MovieServiceImpl movieService,
                          UpdateValidator updateValidator,
                          UserAddMovieValidator userMovieValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.movieService = movieService;
        this.updateValidator = updateValidator;
        this.userMovieValidator = userMovieValidator;
    }

    @GetMapping("/info")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public UserGetInfoResponseDTO info() {
        log.info("User with username: {} check his profile", getUserDetails().getUsername());
        return userService
                .findByUsername(getUserDetails().getUsername())
                .map(this::convertToUserGetInfoResponseDTO)
                .orElse(null);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> update(
            @RequestBody @Valid UserUpdateRequestDTO updateDTO, BindingResult bindingResult) {
        User user = convertToUser(updateDTO);
        if (!getUserDetails().getUsername().equals(user.getUsername())
                || !getUserDetails().getEmail().equals(user.getEmail())) {
            user.setId(getUserDetails().getId());
            updateValidator.validate(user, bindingResult);
            if (bindingResult.hasErrors())
                throw new UpdateUserException(returnMessageForException(bindingResult));
        }
        userService.update(getUserDetails().getUsername(), user);
        log.info("User with username: {} update his profile", getUserDetails().getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> delete() {
        userService.delete(getUserDetails().getUsername());
        log.info("User with username: {} delete himself", getUserDetails().getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/addMovie")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<HttpStatus> addMovie(
            @RequestBody @Valid UserAddMovieRequestDTO movieDTO, BindingResult bindingResult) {
        userMovieValidator.validate(movieDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new MovieNotFoundException(returnMessageForException(bindingResult));
        log.info("User with username: {} add movie with title: {} to his list",
                getUserDetails().getUsername(), movieDTO.getTitle());
        movieService.addMovie(getUserDetails().getUsername(), movieDTO.getTitle());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/getMovies")
    @PreAuthorize("hasRole('USER')")
    public UserGetMoviesResponseDTO getMovies() {
        log.info("User with username: {} check his watched movies", getUserDetails().getUsername());
        return new UserGetMoviesResponseDTO(
                movieService.getMovies(getUserDetails().getUsername()));
    }

    private UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    private UserGetInfoResponseDTO convertToUserGetInfoResponseDTO(User user) {
        return modelMapper.map(user, UserGetInfoResponseDTO.class);
    }

    private User convertToUser(UserUpdateRequestDTO updateDTO) {
        return modelMapper.map(updateDTO, User.class);
    }
}
