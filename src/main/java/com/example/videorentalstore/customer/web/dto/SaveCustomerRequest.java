package com.example.videorentalstore.customer.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

/**
 * Save customer request DTO.
 *
 * @author Sasa Bolic
 */
@Getter
public class SaveCustomerRequest {

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @JsonCreator
    public SaveCustomerRequest(@JsonProperty("first_name") String firstName,
                               @JsonProperty("last_name") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
