package com.example.videorentalstore.film.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WriteFilmRequest {

    private String name;

    private String type;

    private int quantity;

    @JsonCreator
    public WriteFilmRequest(@JsonProperty("name") String name,
                            @JsonProperty("type") String type,
                            @JsonProperty("quantity") Integer quantity) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }
}
