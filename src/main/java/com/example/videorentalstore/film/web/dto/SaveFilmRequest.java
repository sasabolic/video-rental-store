package com.example.videorentalstore.film.web.dto;

import com.example.videorentalstore.core.IsEnum;
import com.example.videorentalstore.film.ReleaseType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Save film request DTO.
 *
 * @author Sasa Bolic
 */
@Getter
public class SaveFilmRequest {

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @IsEnum(enumClass = ReleaseType.class)
    private String type;

    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;

    @JsonCreator
    public SaveFilmRequest(@JsonProperty("title") String title,
                           @JsonProperty("type") String type,
                           @JsonProperty("quantity") Integer quantity) {
        this.title = title;
        this.type = type;
        this.quantity = quantity;
    }
}
