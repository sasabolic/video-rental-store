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
    private List<RentalCommand> rentalCommands;
}
