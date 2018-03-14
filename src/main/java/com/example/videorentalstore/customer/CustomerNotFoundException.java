package com.example.videorentalstore.customer;

/**
 * Exception thrown if {@link Customer} is not found.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
