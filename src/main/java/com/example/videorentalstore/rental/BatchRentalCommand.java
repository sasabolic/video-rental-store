package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Batch rental command.
 */
@AllArgsConstructor
@Getter
public class BatchRentalCommand {

    private Long customerId;
    private Action action;
    private List<Long> rentalIds;

    /**
     * Enumeration for all the actions of {@code BatchRentalCommand}.
     */
    public enum Action {
        PAY,

        RETURN,

        EXTRA_PAY
    }
}
