package com.example.videorentalstore.rental.web.dto;

import com.example.videorentalstore.core.IsEnum;
import com.example.videorentalstore.rental.BatchRentalCommand;
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

    @IsEnum(enumClass = BatchRentalCommand.Action.class)
    private String action;

    @NotEmpty(message = "List of rental requests cannot be empty")
    @Valid
    private List<RentalRequest> rentals;

    @JsonCreator
    public BatchRentalRequest(@JsonProperty("action") String action,
                              @JsonProperty("rentals") List<RentalRequest> rentals) {
        this.action = action;
        this.rentals = rentals;
    }
}
