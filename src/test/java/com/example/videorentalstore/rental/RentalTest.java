package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.FilmDataFixtures;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RentalTest {

    private Rental rental;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp()  {
        rental = RentalDataFixtures.rental();
    }

    @Test
    public void whenNewInstanceThenActiveTrue() {
        assertThat(rental.isActive()).isTrue();
    }

    @Test
    public void givenNegativeDaysRentedWhenNewInstanceThenThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Number of days rented cannot be negative!");

        rental = RentalDataFixtures.rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), -1);
    }

    @Test
    public void givenFilmNullWhenNewInstanceThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Rental's film cannot be null!");

        rental = RentalDataFixtures.rental(null);
    }

    @Test
    public void whenFinishThenActiveFalse() {
        rental.markCompleted();

        assertThat(rental.isActive()).isFalse();
    }

    @Test
    public void givenNullEndDateWhenCalculatePriceThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5);

        final BigDecimal result = rental.calculatePrice();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    public void givenEndDateWhenCalculatePriceThenReturnZero() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, 3);

        rental.markCompleted();

        final BigDecimal result = rental.calculatePrice();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void givenNullEndDateWhenCalculateExtraChargesThenReturnZero() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5);

        final BigDecimal result = rental.calculateExtraCharges();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void givenEndDateBeforeExpireWhenCalculateExtraChargesThenReturnZero() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, 3);

        rental.markCompleted();

        final BigDecimal result = rental.calculateExtraCharges();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void givenEndDateAfterExpireWhenCalculateExtraChargesThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 2, 3);

        rental.markCompleted();

        final BigDecimal result = rental.calculateExtraCharges();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void whenCalculateBonusPointsThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.newReleaseFilm("Matrix 11"));

        final int result = rental.calculateBonusPoints();

        assertThat(result).isEqualTo(2);
    }
}
