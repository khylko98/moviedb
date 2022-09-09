package ua.khylko.moviedb.exception;

public class MovieAlreadyExistException extends RuntimeException {
    public MovieAlreadyExistException(String message) {
        super(message);
    }
}
