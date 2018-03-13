package com.example.videorentalstore.customer.web.dto;

import com.example.videorentalstore.customer.web.CustomerController;
import com.example.videorentalstore.rental.web.CustomerRentalController;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
