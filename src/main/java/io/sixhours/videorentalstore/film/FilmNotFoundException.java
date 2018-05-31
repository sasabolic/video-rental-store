package io.sixhours.videorentalstore.film;

/**
 * Exception thrown if {@link Film} is not found.
 *
 * @author Sasa Bolic
 */
public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(String message) {
        super(message);
    }
}
