package io.sixhours.videorentalstore.film.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Film response DTO.
 *
 * @author Sasa Bolic
 */
@AllArgsConstructor
@Getter
public class FilmResponse {

    private Long id;

    private String title;

    private String type;

    private int quantity;
}
