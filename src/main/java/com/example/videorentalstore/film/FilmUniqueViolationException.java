package com.example.videorentalstore.film;

/**
 * Exception thrown in case {@code Film} is not unique.
 */
public class FilmUniqueViolationException extends RuntimeException {

    public FilmUniqueViolationException(String message) {
        super(message);
    }
}
