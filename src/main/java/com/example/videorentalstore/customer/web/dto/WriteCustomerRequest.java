package com.example.videorentalstore.customer.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

/**
 * Create/update customer request DTO.
 */
@Getter
public class WriteCustomerRequest {

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @JsonCreator
    public WriteCustomerRequest(@JsonProperty("first_name") String firstName,
                                @JsonProperty("last_name") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
