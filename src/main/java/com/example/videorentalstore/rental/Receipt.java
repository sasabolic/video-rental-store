package com.example.videorentalstore.rental;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class Receipt {
    private BigDecimal amount;

    private List<Rental> rentals;

    public Receipt(BigDecimal amount, List<Rental> rentals) {
        this.amount = amount;
        this.rentals = rentals;
    }
}
