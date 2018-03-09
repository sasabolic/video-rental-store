package com.example.videorentalstore.rental.web.dto;

import com.example.videorentalstore.rental.ReturnRentalCommand;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class ReturnBackRentalRequest {

    @NotNull(message = "Rental id cannot be null")
    private Long rentalId;

    @JsonCreator
    public ReturnBackRentalRequest(@JsonProperty("rental_id") Long rentalId) {
        this.rentalId = rentalId;
    }

    public ReturnRentalCommand toReturnRentalCommand() {
        return new ReturnRentalCommand(this.rentalId);
    }
}
