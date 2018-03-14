package com.example.videorentalstore.film;

/**
 * Exception thrown if {@link Film} is not found.
 */
public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(String message) {
        super(message);
    }
}
