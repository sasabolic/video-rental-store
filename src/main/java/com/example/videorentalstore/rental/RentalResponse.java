package com.example.videorentalstore.rental;

import java.math.BigDecimal;
import java.util.List;

public class RentalResponse {
    private BigDecimal amount;

    private List<Rental> rentals;

    public RentalResponse(BigDecimal amount, List<Rental> rentals) {
        this.amount = amount;
        this.rentals = rentals;
    }
}
