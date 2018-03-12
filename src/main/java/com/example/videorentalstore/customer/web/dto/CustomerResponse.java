package com.example.videorentalstore.customer.web.dto;

import com.example.videorentalstore.customer.web.CustomerController;
import com.example.videorentalstore.film.web.FilmController;
import com.example.videorentalstore.rental.web.CustomerRentalController;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Customer response DTO.
 */
@Relation(collectionRelation = "customers")
@Getter
public class CustomerResponse extends ResourceSupport {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("bonus_points")
    private long bonusPoints;

    public CustomerResponse(Long id, String firstName, String lastName, long bonusPoints) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bonusPoints = bonusPoints;

        add(linkTo(methodOn(CustomerController.class).get(id)).withSelfRel());
        add(linkTo(methodOn(CustomerRentalController.class).getAll(id, null)).withRel("rentals"));
    }
}
