package com.example.videorentalstore.rental.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.core.Relation;

import java.time.Instant;

/**
 * Rental response DTO.
 */
@Relation(collectionRelation = "rentals")
@AllArgsConstructor
@Getter
public class RentalResponse {

    private Long id;

    @JsonProperty("film_title")
    private String filmTitle;

    @JsonProperty("days_rented")
    private int daysRented;

    private String status;

    @JsonProperty("start_date")
    private Instant startDate;

    @JsonProperty("return_date")
    private Instant returnDate;
}
