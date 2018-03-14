package com.example.videorentalstore.film;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class FilmTest {

    private Film film;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp()  {
        film = FilmDataFixtures.regularReleaseFilm();
    }

    @Test
    public void whenNewInstanceThenActiveTrue() {
        assertThat(film.isActive()).isTrue();
    }

    @Test
    public void givenNegativeQuantityWhenNewInstanceThenThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Number of film copies cannot be negative!");

        film = FilmDataFixtures.newReleaseFilm("Matrix 11", -1);
    }

    @Test
    public void givenTitleNullWhenNewInstanceThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Film's title cannot be null!");

        film = FilmDataFixtures.newReleaseFilm(null);
    }

    @Test
    public void givenTypeNullWhenNewInstanceThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Film's type cannot be null!");

        film = FilmDataFixtures.film("Matrix 11", null, 10);
    }

    @Test
    public void whenUpdateThenFieldsChanged() {
        final String title = "NewTitle";
        final String type = "NEW_RELEASE";
        final int quantity = 150;

        film.update(title, type, quantity);

        assertThat(film).hasFieldOrPropertyWithValue("title", title);
        assertThat(film).hasFieldOrPropertyWithValue("type", ReleaseType.NEW_RELEASE);
        assertThat(film).hasFieldOrPropertyWithValue("quantity", quantity);
    }

    @Test
    public void whenUpdateNonExistingTypeThenThrowException() {
        final String title = "NewTitle";
        final String type = "NON_EXISTING";
        final int quantity = 150;

        thrown.expect(IllegalArgumentException.class);

        film.update(title, type, quantity);
    }

    @Test
    public void whenDeactivateThenActiveFalse() {
        film.deactivate();

        assertThat(film.isActive()).isFalse();
    }

    @Test
    public void whenChangeByPositiveValueThenQuantityIncreased() {
        final int before = film.getQuantity();

        film.changeQuantityBy(10);

        assertThat(film.getQuantity()).isEqualTo(before + 10);
    }

    @Test
    public void whenChangeByNegativeValueThenQuantityDecreased() {
        final int before = film.getQuantity();

        film.changeQuantityBy(-10);

        assertThat(film.getQuantity()).isEqualTo(before - 10);
    }

    @Test
    public void whenChangeByGivesNegativeValueThenThrowException() {
        final int before = film.getQuantity();

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Number of film copies cannot be negative!");

        film.changeQuantityBy(-(before + 1));
    }

    @Test
    public void whenTakeThenQuantityDecreased() {
        final int before = film.getQuantity();

        film.take();

        assertThat(film.getQuantity()).isEqualTo(before - 1);
    }

    @Test
    public void givenQuantityIsZeroWhenTakeThenThrowException() {
        film = FilmDataFixtures.newReleaseFilm("Matrix 11", 0);

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("There is not any available copy of the film '" + film.getTitle() + "'");

        film.take();
    }

    @Test
    public void whenReturnBackThenQuantityIncreased() {
        final int before = film.getQuantity();

        film.putBack();

        assertThat(film.getQuantity()).isEqualTo(before + 1);
    }

    @Test
    public void whenCalculatePriceThenReturnCorrectResult() {
        final Money result = film.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(240, result.getCurrency()));
    }

    @Test
    public void whenCalculateBonusPointsThenReturnCorrectResult() {
        final int result = film.calculateBonusPoints(10);

        assertThat(result).isEqualTo(1);
    }
}
