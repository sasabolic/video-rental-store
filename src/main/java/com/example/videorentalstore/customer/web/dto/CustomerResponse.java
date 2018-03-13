package com.example.videorentalstore.customer.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Customer response DTO.
 */
@AllArgsConstructor
@Getter
public class CustomerResponse {

    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("bonus_points")
    private long bonusPoints;
}
