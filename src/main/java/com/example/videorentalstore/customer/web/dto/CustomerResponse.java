package com.example.videorentalstore.customer.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    // TODO: 3/9/18 Hateoas to link of rentals
//    private List<Rental> rentals = new ArrayList<>();
}
