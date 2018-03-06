package com.example.videorentalstore.film.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class WriteFilmRequest {

    private String title;
    private String type;
    private int quantity;

    @JsonCreator
    public WriteFilmRequest(@JsonProperty("title") String title,
                            @JsonProperty("type") String type,
                            @JsonProperty("quantity") Integer quantity) {
        this.title = title;
        this.type = type;
        this.quantity = quantity;
    }
}
