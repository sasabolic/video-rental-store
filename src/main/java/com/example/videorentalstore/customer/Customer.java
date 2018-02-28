package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.Rental;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer {

    private List<Rental> rentals = new ArrayList<>();

    public void addRental(Rental rental) {
        this.rentals.add(rental);
    }

    public BigDecimal calculate() {
        return this.rentals.stream()
                .filter(r -> Rental.Status.RENTED.equals(r.getStatus()))
                .map(r -> r.calculatePrice())
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
    }

    public BigDecimal calculateExtraCharges() {
        return this.rentals.stream()
                .filter(r -> Rental.Status.RETURNED.equals(r.getStatus()))
                .map(r -> r.calculateExtraCharges())
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
    }

    public int calculateBonusPoint() {
        return this.rentals.stream()
                .filter(r -> Rental.Status.RETURNED.equals(r.getStatus()))
                .map(r -> r.calculateBonusPoints())
                .reduce(0, (x, y) -> x + y);
    }
}
