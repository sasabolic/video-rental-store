package com.example.videorentalstore.film;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class FilmTest {

    private int daysRented = 10;

    @Test
    public void givenNewReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final Film newReleaseFilm = FilmDataFixtures.newReleaseFilm("Matrix 11");

        final BigDecimal result = newReleaseFilm.calculatePrice(daysRented);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(400));
    }

    @Test
    public void givenRegularReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final Film regularReleaseFilm = FilmDataFixtures.regularReleaseFilm("Spider Man");

        final BigDecimal result = regularReleaseFilm.calculatePrice(daysRented);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(240));
    }

    @Test
    public void givenOldReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final Film oldReleaseFilm = FilmDataFixtures.oldReleaseFilm("Out of Africa");

        final BigDecimal result = oldReleaseFilm.calculatePrice(daysRented);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(180));
    }

    @Test
    public void givenNewReleaseWhenCalculateBonusPointsThenReturnCorrectResult() {
        final Film newReleaseFilm = FilmDataFixtures.newReleaseFilm("Matrix 11");

        final int result = newReleaseFilm.calculateBonusPoints();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(2);
    }

    @Test
    public void givenRegularReleaseWhenCalculateBonusPointsThenReturnCorrectResult() {
        final Film regularReleaseFilm = FilmDataFixtures.regularReleaseFilm("Spider Man");

        final int result = regularReleaseFilm.calculateBonusPoints();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(1);
    }

    @Test
    public void givenOldReleaseWhenCalculateBonusPointsThenReturnCorrectResult() {
        final Film oldReleaseFilm = FilmDataFixtures.oldReleaseFilm("Out of Africa");

        final int result = oldReleaseFilm.calculateBonusPoints();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(1);
    }
}
