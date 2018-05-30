package com.example.videorentalstore.rental;

/**
 * Exception thrown if {@link Rental} is not found.
 *
 * @author Sasa Bolic
 */
public class RentalNotFoundException extends RuntimeException {

    public RentalNotFoundException(String message) {
        super(message);
    }
}
