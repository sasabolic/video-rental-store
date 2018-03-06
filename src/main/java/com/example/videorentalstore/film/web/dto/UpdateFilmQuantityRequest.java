package com.example.videorentalstore.film.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateFilmQuantityRequest {

    private int quantity;

    @JsonCreator
    public UpdateFilmQuantityRequest(@JsonProperty("quantity") Integer quantity) {
        this.quantity = quantity;
    }
}
