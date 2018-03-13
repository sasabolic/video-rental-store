package com.example.videorentalstore.rental;

import com.example.videorentalstore.film.Film;
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
    public void whenNewInstanceThenStatusReserved() {
        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.RESERVED);
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
    public void whenMarkUpFrontPaymentExpectedThenStatusUpFrontPaymentExpected() {
        rental.markUpFrontPaymentExpected();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.UP_FRONT_PAYMENT_EXPECTED);
    }

    @Test
    public void givenInvalidStateWhenMarkUpFrontPaymentExpectedThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as UP_FRONT_PAYMENT_EXPECTED that is currently not RESERVED!");

        rental.markUpFrontPaymentExpected();
        rental.markInProcess();

        rental.markUpFrontPaymentExpected();
    }

    @Test
    public void whenMarkInProcessThenStatusInProcess() {
        rental.markUpFrontPaymentExpected();

        rental.markInProcess();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.IN_PROCESS);
    }

    @Test
    public void givenInvalidStateWhenMarkInProcessThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as IN_PROCESS that is currently not UP_FRONT_PAYMENT_EXPECTED!");

        rental.markInProcess();
    }

    @Test
    public void whenMarkReturnedThenStatusReturned() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();

        rental.markReturned();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.RETURNED);
    }

    @Test
    public void givenInvalidStateWhenMarkReturnedThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as RETURNED that is currently not IN_PROCESS!");

        rental.markReturned();
    }

    @Test
    public void whenMarkLatePaymentExpectedThenStatusLatePaymentExpected() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();
        rental.markReturned();

        rental.markLatePaymentExpected();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.LATE_PAYMENT_EXPECTED);
    }

    @Test
    public void givenInvalidStateWhenMarkLatePaymentExpectedThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as LATE_PAYMENT_EXPECTED that is currently not RETURNED!");

        rental.markLatePaymentExpected();
    }

    @Test
    public void whenMarkCompletedThenStatusCompleted() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();
        rental.markReturned();
        rental.markLatePaymentExpected();

        rental.markCompleted();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.COMPLETED);
    }

    @Test
    public void givenInvalidStateWhenMarkCompletedThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as COMPLETED that is currently not LATE_PAYMENT_EXPECTED! Current status: RESERVED.");

        rental.markCompleted();
    }

    @Test
    public void whenIsReservedThenTrue() {

        final boolean result = rental.isReserved();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsReservedThenFalse() {
        rental.markUpFrontPaymentExpected();

        final boolean result = rental.isReserved();

        assertThat(result).isFalse();
    }

    @Test
    public void whenIsUpFrontPaymentExpectedThenTrue() {
        rental.markUpFrontPaymentExpected();

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsUpFrontPaymentExpectedThenFalse() {

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isFalse();
    }

    @Test
    public void whenIsInProcessThenTrue() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();

        final boolean result = rental.isInProcess();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsInProcessThenFalse() {

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isFalse();
    }

    @Test
    public void whenIsLatePaymentExpectedThenTrue() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();
        rental.markReturned();
        rental.markLatePaymentExpected();

        final boolean result = rental.isLatePaymentExpected();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsLatePaymentExpectedThenFalse() {

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isFalse();
    }

    @Test
    public void givenDifferentStatusWhenIsNotCompletedThenTrue() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();
        rental.markReturned();

        final boolean result = rental.isNotCompleted();

        assertThat(result).isTrue();
    }

    @Test
    public void whenIsNotCompletedThenFalse() {
        rental.markUpFrontPaymentExpected();
        rental.markInProcess();
        rental.markReturned();
        rental.markLatePaymentExpected();
        rental.markCompleted();

        final boolean result = rental.isNotCompleted();

        assertThat(result).isFalse();
    }

    @Test
    public void whenHasStatusThenTrue() {
        rental.markUpFrontPaymentExpected();

        final boolean result = rental.hasStatus(Rental.Status.UP_FRONT_PAYMENT_EXPECTED);

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenHasStatusThenFalse() {
        rental.markUpFrontPaymentExpected();

        final boolean result = rental.hasStatus(Rental.Status.IN_PROCESS);

        assertThat(result).isFalse();
    }

    @Test
    public void whenApplyThenChanged() {
        final Rental rental = RentalDataFixtures.rental();
        final boolean before = rental.isActive();

        rental.apply(Rental::deactivate);

        assertThat(rental.isActive()).isNotEqualTo(before);
    }

    @Test
    public void whenDeactivateThenActiveFalse() {
        rental.deactivate();

        assertThat(rental.isActive()).isFalse();
    }

    @Test
    public void givenInvalidStateWhenDeactivateThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot deactivate rental that is currently not RESERVED!");

        rental.markUpFrontPaymentExpected();

        rental.deactivate();
    }

    @Test
    public void whenCalculatePriceThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5);

        final BigDecimal result = rental.calculatePrice();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(90));
    }

    @Test
    public void whenCalculateExtraChargesThenReturnCorrectResult() {
        rental = RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 2, 3);

        rental.markUpFrontPaymentExpected();
        rental.markInProcess();
        rental.markReturned();

        final BigDecimal result = rental.calculateExtraCharges();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void givenNullReturnDateWhenCalculateExtraChargesThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Cannot create late charges if RETURN DATE is not set.");

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
