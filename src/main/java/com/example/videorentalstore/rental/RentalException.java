package com.example.videorentalstore.rental;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown in case creation or returning batch of{@link Rental} is not successful.
 *
 * @author Sasa Bolic
 */
@Getter
public class RentalException extends RuntimeException {

    private final List<Exception> exceptions = new ArrayList<>();

    public RentalException(String message) {
        super(message);
    }

    public RentalException(String message, List<Exception> exceptions) {
        this(message);
        this.exceptions.addAll(exceptions);
    }

    public boolean isEmpty() {
        return this.exceptions.isEmpty();
    }
}
