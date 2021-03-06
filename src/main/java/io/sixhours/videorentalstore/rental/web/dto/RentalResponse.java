package io.sixhours.videorentalstore.rental.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * Rental response DTO.
 *
 * @author Sasa Bolic
 */
@AllArgsConstructor
@Getter
public class RentalResponse {

    private Long id;

    @JsonProperty("film_title")
    private String filmTitle;

    @JsonProperty("days_rented")
    private int daysRented;

    @JsonProperty("start_date")
    private Instant startDate;

    @JsonProperty("end_date")
    private Instant endDate;
}
