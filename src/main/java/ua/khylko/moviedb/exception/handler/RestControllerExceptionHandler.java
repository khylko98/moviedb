package ua.khylko.moviedb.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.khylko.moviedb.exception.*;
import ua.khylko.moviedb.exception.manager.ExceptionResponse;

@ControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler({SignUpException.class})
    private ResponseEntity<ExceptionResponse> handleException(SignUpException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    private ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = new ExceptionResponse(
                "username/password - Incorrect username or password",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UpdateUserException.class})
    private ResponseEntity<ExceptionResponse> handleException(UpdateUserException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MovieNotFoundException.class})
    private ResponseEntity<ExceptionResponse> handleException(MovieNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class})
    private ResponseEntity<ExceptionResponse> handleException(UserNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MovieAlreadyExistException.class})
    private ResponseEntity<ExceptionResponse> handleException(MovieAlreadyExistException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
