package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RentalInfo {

    private Long filmId;
    private int daysRented;
}
