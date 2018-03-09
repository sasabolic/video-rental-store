package com.example.videorentalstore.rental.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Receipt response DTO.
 */
@AllArgsConstructor
@Getter
public class ReceiptResponse {

    private BigDecimal amount;
    private List<RentalResponse> rentals;
}
