package io.sixhours.videorentalstore.rental.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.sixhours.videorentalstore.rental.RentalInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Create rental request DTO.
 *
 * @author Sasa Bolic
 */
@Getter
@ToString
@EqualsAndHashCode
public class CreateRentalRequest {

    @NotNull(message = "Film id cannot be null")
    private Long filmId;

    @NotNull(message = "Days rented cannot be null")
    @Min(value = 1, message = "Days rented must have minimum value of: 1")
    private Integer daysRented;

    @JsonCreator
    public CreateRentalRequest(@JsonProperty("film_id") Long filmId,
                               @JsonProperty("days_rented") Integer daysRented) {
        this.filmId = filmId;
        this.daysRented = daysRented;
    }

    public RentalInfo toRentalInfo() {
        return new RentalInfo(this.filmId, this.daysRented);
    }
}
