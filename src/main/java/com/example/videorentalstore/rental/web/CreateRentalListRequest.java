package com.example.videorentalstore.rental.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Create list of rentals request DTO.
 */
@Getter
@ToString
public class CreateRentalListRequest {

    @NotNull(message = "List of create rental requests cannot be null")
    @NotEmpty(message = "List of create rental requests cannot be empty")
    @Valid
    private List<CreateRentalRequest> createRentalRequests;

    @JsonCreator
    public CreateRentalListRequest(List<CreateRentalRequest> createRentalRequests) {
        this.createRentalRequests = createRentalRequests;
    }
}
