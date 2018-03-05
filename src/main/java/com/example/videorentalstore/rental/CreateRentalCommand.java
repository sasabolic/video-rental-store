package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateRentalCommand {

    private Long filmId;
    private int daysRented;
}
