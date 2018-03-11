package com.example.videorentalstore.rental.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Return rental request DTO.
 */
@Getter
@ToString
public class ReturnRentalRequest {

    @NotNull(message = "Rental id cannot be null")
    private Long rentalId;

    @JsonCreator
    public ReturnRentalRequest(@JsonProperty("rental_id") Long rentalId) {
        this.rentalId = rentalId;
    }
}
