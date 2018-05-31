package io.sixhours.videorentalstore.rental;

import io.sixhours.videorentalstore.film.Film;
import io.sixhours.videorentalstore.film.FilmDataFixtures;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
    public void whenNewInstanceThenEndDateNull() {
        assertThat(rental.getEndDate()).isNull();
    }

    @Test
    public void whenNewInstanceThenFilmQuantityDecreased() {
        final Film film = FilmDataFixtures.newReleaseFilm();
        final int before = film.getQuantity();

        rental = RentalDataFixtures.rental(film);

        assertThat(this.rental.getFilm().getQuantity()).isEqualTo(before - 1);
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
    public void whenMarkReturnedThenActiveFalse() {
        rental.markReturned();

        assertThat(rental.isActive()).isFalse();
    }

    @Test
    public void whenMarkReturnedThenEndDateIsNotNull() {
        rental.markReturned();

        assertThat(rental.getEndDate()).isNotNull();
    }

    @Test
    public void whenMarkReturnedThenEndDateIsToday() {
        rental.markReturned();

        assertThat(ChronoUnit.DAYS.between(rental.getEndDate(), Instant.now())).isEqualTo(0);
    }

    @Test
    public void whenCalculatePriceThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5);

        final Money result = rental.calculatePrice();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(90, result.getCurrency()));
    }

    @Test
    public void whenCalculateExtraChargesThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 2, 3);

        rental.markReturned();

        final Money result = rental.calculateExtraCharges();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(30, result.getCurrency()));
    }

    @Test
    public void givenNullReturnDateWhenCalculateExtraChargesThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Cannot create late charges if END DATE is not set.");

        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5);

        rental.calculateExtraCharges();
    }

    @Test
    public void whenCalculateBonusPointsThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.newReleaseFilm("Matrix 11"));

        final int result = rental.calculateBonusPoints();

        assertThat(result).isEqualTo(2);
    }
}
