package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class BatchRental {

    private BigDecimal amount;
    private List<Rental> rentals;

    public BatchRental(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
