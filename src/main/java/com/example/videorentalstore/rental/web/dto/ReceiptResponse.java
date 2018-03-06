package com.example.videorentalstore.rental.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class ReceiptDto {

    private BigDecimal amount;
    private List<RentalDto> rentals;
}
