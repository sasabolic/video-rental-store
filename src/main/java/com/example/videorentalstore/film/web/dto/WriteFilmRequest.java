package com.example.videorentalstore.film.web.dto;

import com.example.videorentalstore.core.ValidReleaseType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Create/update film request DTO.
 */
@Getter
public class WriteFilmRequest {

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @ValidReleaseType
    private String type;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @JsonCreator
    public WriteFilmRequest(@JsonProperty("title") String title,
                            @JsonProperty("type") String type,
                            @JsonProperty("quantity") Integer quantity) {
        this.title = title;
        this.type = type;
        this.quantity = quantity;
    }
}
