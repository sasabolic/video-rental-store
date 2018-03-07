package com.example.videorentalstore.customer.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WriteCustomerRequest {

    private String firstName;
    private String lastName;

    @JsonCreator
    public WriteCustomerRequest(@JsonProperty("first_name") String firstName,
                                @JsonProperty("last_name") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
