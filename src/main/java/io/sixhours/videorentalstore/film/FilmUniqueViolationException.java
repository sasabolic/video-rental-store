package io.sixhours.videorentalstore.film;

/**
 * Exception thrown in case {@code Film} is not unique.
 *
 * @author Sasa Bolic
 */
public class FilmUniqueViolationException extends RuntimeException {

    public FilmUniqueViolationException(String message) {
        super(message);
    }
}
