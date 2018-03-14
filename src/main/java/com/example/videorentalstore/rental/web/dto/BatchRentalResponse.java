package com.example.videorentalstore.rental.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.util.List;

/**
 * Batch rental response DTO.
 */
@AllArgsConstructor
@Getter
public class BatchRentalResponse {

    private Money amount;
    private List<RentalResponse> rentals;
}
