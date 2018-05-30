package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Rental information.
 *
 * @author Sasa Bolic
 */
@AllArgsConstructor
@Getter
public class RentalInfo {

    private Long filmId;
    private int daysRented;
}
