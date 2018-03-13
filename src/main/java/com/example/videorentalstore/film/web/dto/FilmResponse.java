package com.example.videorentalstore.film.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Film response DTO.
 */
@AllArgsConstructor
@Getter
public class FilmResponse {

    private Long id;

    private String title;

    private String type;

    private int quantity;
}
