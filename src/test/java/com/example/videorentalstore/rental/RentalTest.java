package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmDataFixtures;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class RentalTest {

    @Test
    public void giveRegularReleaseRentedFor5DaysWhenCalculatePriceThenReturn90() {
        Film film = FilmDataFixtures.regularReleaseFilm("Spider Man");

        Rental rental = new Rental(film, 5);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    public void giveRegularReleaseRentedFor3DaysWhenCalculatePriceThenReturn30() {
        Film film = FilmDataFixtures.regularReleaseFilm("Spider Man 2");

        Rental rental = new Rental(film, 3);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void giveRegularReleaseRentedFor1DaysWhenCalculatePriceThenReturn30() {
        Film film = FilmDataFixtures.regularReleaseFilm("Spider Man 2");

        Rental rental = new Rental(film, 1);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void giveNewReleaseRentedFor1DaysWhenCalculatePriceThenReturn40() {
        Film film = FilmDataFixtures.newReleaseFilm("Matrix 11");

        Rental rental = new Rental(film, 1);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(40));
    }

    @Test
    public void giveNewReleaseRentedFor3DaysWhenCalculatePriceThenReturn120() {
        Film film = FilmDataFixtures.newReleaseFilm("Matrix 11");

        Rental rental = new Rental(film, 3);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(120));
    }

    @Test
    public void givenOldReleaseFilmRentedFor5DaysWhenCalculatePriceThenReturn30() {
        Film film = FilmDataFixtures.oldReleaseFilm("Out of Africa");

        Rental rental = new Rental(film, 5);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void givenOldReleaseFilmRentedFor7DaysWhenCalculatePriceThenReturn90() {
        Film film = FilmDataFixtures.oldReleaseFilm("Out of Africa");

        Rental rental = new Rental(film, 7);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    public void givenOldReleaseFilmRentedFor1DayWhenCalculatePriceThenReturn30() {
        Film film = FilmDataFixtures.oldReleaseFilm("Out of Africa");

        Rental rental = spy(new Rental(film, 1));

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }
}
