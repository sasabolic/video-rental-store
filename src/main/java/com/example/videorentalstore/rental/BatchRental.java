package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.util.List;

@AllArgsConstructor
@Getter
public class BatchRental {

    private Money amount;
    private List<Rental> rentals;

    public BatchRental(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
