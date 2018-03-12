package com.example.videorentalstore.rental;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RentalResult {

    private Rental.Status status;
    private List<Rental> rentals;

    public RentalResult(Rental.Status status, List<Rental> rentals) {
        this.status = status;
        this.rentals = rentals.stream().filter(r -> r.hasStatus(status)).collect(Collectors.toList());
    }
}
