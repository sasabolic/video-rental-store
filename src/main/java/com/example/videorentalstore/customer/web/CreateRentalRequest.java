package com.example.videorentalstore.customer.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RentalItem {

    private Long filmId;
    private int daysRented;

    @JsonCreator
    public RentalItem(@JsonProperty("film_id") Long filmId,
                      @JsonProperty("days_rented") int daysRented) {
        this.filmId = Long.valueOf(filmId);
        this.daysRented = daysRented;
    }
}
