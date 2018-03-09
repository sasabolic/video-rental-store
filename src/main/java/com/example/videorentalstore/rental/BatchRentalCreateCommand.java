package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Batch rental create command.
 */
@AllArgsConstructor
@Getter
public class BatchRentalCreateCommand {

    private Long customerId;
    private List<RentalInfo> rentalInfos;
}
