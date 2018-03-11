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
    public void whenNewInstanceThenStatusUpFrontPaymentExpected() {
        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.UP_FRONT_PAYMENT_EXPECTED);
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
    public void whenMarkPaidUpFrontThenStatusPaidUpFront() {
        rental.markPaidUpFront();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.PAID_UP_FRONT);
    }

    @Test
    public void givenInvalidStateWhenMarkPaidUpFrontThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as PAID_UP_FRONT that is currently not UP_FRONT_PAYMENT_EXPECTED!");

        rental.markPaidUpFront();
        rental.markInProcess();

        rental.markPaidUpFront();
    }

    @Test
    public void whenMarkInProcessThenStatusInProcess() {
        rental.markPaidUpFront();

        rental.markInProcess();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.IN_PROCESS);
    }

    @Test
    public void givenInvalidStateWhenMarkInProcessThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as IN_PROCESS that is currently not PAID_UP_FRONT!");

        rental.markInProcess();
    }

    @Test
    public void whenMarkReturnedThenStatusReturned() {
        rental.markPaidUpFront();
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
        rental.markPaidUpFront();
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
    public void whenMarkPayedLateThenStatusPayedLate() {
        rental.markPaidUpFront();
        rental.markInProcess();
        rental.markReturned();
        rental.markLatePaymentExpected();

        rental.markPayedLate();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.PAID_LATE);
    }

    @Test
    public void givenInvalidStateWhenMarkPaidLatetExpectedThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as PAID_LATE that is currently not LATE_PAYMENT_EXPECTED!");

        rental.markPayedLate();
    }

    @Test
    public void whenMarkCompletedThenStatusCompleted() {
        rental.markPaidUpFront();
        rental.markInProcess();
        rental.markReturned();
        rental.markLatePaymentExpected();
        rental.markPayedLate();

        rental.markCompleted();

        assertThat(rental.getStatus()).isEqualByComparingTo(Rental.Status.COMPLETED);
    }

    @Test
    public void givenInvalidStateWhenMarkCompletedThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot mark rental as COMPLETED that is currently not PAID_LATE! Current status: UP_FRONT_PAYMENT_EXPECTED.");

        rental.markCompleted();
    }

    @Test
    public void whenIsUpFrontPaymentExpectedThenTrue() {

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsUpFrontPaymentExpectedThenFalse() {
        rental.markPaidUpFront();

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isFalse();
    }

    @Test
    public void whenIsPaidUpFrontThenTrue() {
        rental.markPaidUpFront();

        final boolean result = rental.isPaidUpFront();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsPaidUpFrontThenFalse() {

        final boolean result = rental.isPaidUpFront();

        assertThat(result).isFalse();
    }

    @Test
    public void whenIsLatePaymentExpectedThenTrue() {

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenIsLatePaymentExpectedThenFalse() {
        rental.markPaidUpFront();
        rental.markInProcess();
        rental.markReturned();

        final boolean result = rental.isUpFrontPaymentExpected();

        assertThat(result).isFalse();
    }

    @Test
    public void givenDifferentStatusWhenIsNotCompletedThenTrue() {
        rental.markPaidUpFront();
        rental.markInProcess();
        rental.markReturned();

        final boolean result = rental.isNotCompleted();

        assertThat(result).isTrue();
    }

    @Test
    public void whenIsNotCompletedThenFalse() {
        rental.markPaidUpFront();
        rental.markInProcess();
        rental.markReturned();
        rental.markLatePaymentExpected();
        rental.markPayedLate();
        rental.markCompleted();

        final boolean result = rental.isNotCompleted();

        assertThat(result).isFalse();
    }

    @Test
    public void whenHasStatusThenTrue() {
        rental.markPaidUpFront();

        final boolean result = rental.hasStatus(Rental.Status.PAID_UP_FRONT);

        assertThat(result).isTrue();
    }

    @Test
    public void givenDifferentStatusWhenHasStatusThenFalse() {
        rental.markPaidUpFront();

        final boolean result = rental.hasStatus(Rental.Status.IN_PROCESS);

        assertThat(result).isFalse();
    }

    @Test
    public void whenDeactivateThenActiveFalse() {
        rental.deactivate();

        assertThat(rental.isActive()).isFalse();
    }

    @Test
    public void givenInvalidStateWhenDeactivateThenThrowError() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Cannot deactivate rental that is currently not UP_FRONT_PAYMENT_EXPECTED!");

        rental.markPaidUpFront();

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

        rental.markPaidUpFront();
        rental.markInProcess();
        rental.markReturned();

        final BigDecimal result = rental.calculateExtraCharges();

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    public void givenNullReturnDateWhenCalculateExtraChargesThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Cannot calculate late charges if RETURN DATE is not set.");

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
