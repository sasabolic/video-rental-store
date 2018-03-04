package com.example.videorentalstore.customer.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class RentalUpdateRequest {

    private Action action;

    private List<CreateRentalRequest> rentals;

    @JsonCreator
    public RentalUpdateRequest(@JsonProperty("action") Action action,
                               @JsonProperty("rentals") List<CreateRentalRequest> rentals) {
        this.action = action;
        this.rentals = rentals;
    }

    enum Action {
        PAY,

        RETURN
    }
}
