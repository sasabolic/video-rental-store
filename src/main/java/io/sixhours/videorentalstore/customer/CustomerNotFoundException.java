package io.sixhours.videorentalstore.customer;

/**
 * Exception thrown if {@link Customer} is not found.
 *
 * @author Sasa Bolic
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
