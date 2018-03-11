package com.example.videorentalstore.rental.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Batch rental request DTO.
 */
@Getter
public class BatchRentalRequest {

    @NotEmpty(message = "List of rental requests cannot be empty")
    @Valid
    private List<RentalRequest> rentals;

    @JsonCreator
    public BatchRentalRequest(@JsonProperty("rentals") List<RentalRequest> rentals) {
        this.rentals = rentals;
    }
}
