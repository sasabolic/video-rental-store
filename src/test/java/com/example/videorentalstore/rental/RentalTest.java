package com.example.videorentalstore.rental;

import com.example.videorentalstore.inventory.Film;
import com.example.videorentalstore.inventory.NewReleaseFilm;
import com.example.videorentalstore.inventory.OldReleaseFilm;
import com.example.videorentalstore.inventory.RegularReleaseFilm;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class RentalTest {

    @Test
    public void giveRegularReleaseRentedFor5DaysWhenCalculatePriceThenReturn90() {
        Film film = new RegularReleaseFilm("Spider Man");

        Rental rental = new Rental(film, 5);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    public void giveRegularReleaseRentedFor3DaysWhenCalculatePriceThenReturn30() {
        Film film = new RegularReleaseFilm("Spider Man 2");

        Rental rental = new Rental(film, 3);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void giveRegularReleaseRentedFor1DaysWhenCalculatePriceThenReturn30() {
        Film film = new RegularReleaseFilm("Spider Man 2");

        Rental rental = new Rental(film, 1);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void giveNewReleaseRentedFor1DaysWhenCalculatePriceThenReturn40() {
        Film film = new NewReleaseFilm("Matrix 11");

        Rental rental = new Rental(film, 1);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(40));
    }

    @Test
    public void giveNewReleaseRentedFor3DaysWhenCalculatePriceThenReturn120() {
        Film film = new NewReleaseFilm("Matrix 11");

        Rental rental = new Rental(film, 3);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(120));
    }

    @Test
    public void givenOldReleaseFilmRentedFor5DaysWhenCalculatePriceThenReturn30() {
        Film film = new OldReleaseFilm("Out of Africa");

        Rental rental = new Rental(film, 5);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void givenOldReleaseFilmRentedFor7DaysWhenCalculatePriceThenReturn90() {
        Film film = new OldReleaseFilm("Out of Africa");

        Rental rental = new Rental(film, 7);

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    public void givenOldReleaseFilmRentedFor1DayWhenCalculatePriceThenReturn30() {
        Film film = new OldReleaseFilm("Out of Africa");

        Rental rental = spy(new Rental(film, 1));

        final BigDecimal price = rental.calculatePrice();

        assertThat(price).isEqualByComparingTo(BigDecimal.valueOf(30));
    }
}
