package com.example.videorentalstore.rental.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Return rental request DTO.
 *
 * @author Sasa Bolic
 */
@Getter
@ToString
public class ReturnRentalRequest {

    @NotNull(message = "Rental id cannot be null")
    private Long id;

    @JsonCreator
    public ReturnRentalRequest(@JsonProperty("id") Long id) {
        this.id = id;
    }
}
