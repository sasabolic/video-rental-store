package com.example.videorentalstore.film.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

/**
 * Update film's quantity request DTO.
 */
@Getter
public class UpdateFilmQuantityRequest {

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @JsonCreator
    public UpdateFilmQuantityRequest(@JsonProperty("quantity") Integer quantity) {
        this.quantity = quantity;
    }
}
