package io.sixhours.videorentalstore.customer;

import io.sixhours.videorentalstore.rental.Rental;
import io.sixhours.videorentalstore.rental.RentalDataFixtures;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {

    private Customer customer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp()  {
        customer = CustomerDataFixtures.customer();
    }

    @Test
    public void whenNewInstanceThenActiveTrue() {
        assertThat(customer.isActive()).isTrue();
    }

    @Test
    public void givenFirstNameNullWhenNewInstanceThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Customer's first name cannot be null!");

        customer = CustomerDataFixtures.customer(null, "Smith");
    }

    @Test
    public void givenLastNameNullWhenNewInstanceThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Customer's last name cannot be null!");

        customer = CustomerDataFixtures.customer("John", null);
    }

    @Test
    public void whenUpdateThenFieldsChanged() {
        final String newFirstName = "New";
        final String newLastName = "Newish";

        customer.update(newFirstName, newLastName);

        assertThat(customer).hasFieldOrPropertyWithValue("firstName", newFirstName);
        assertThat(customer).hasFieldOrPropertyWithValue("lastName", newLastName);
    }

    @Test
    public void whenDeactivateThenActiveFalse() {
        customer.deactivate();

        assertThat(customer.isActive()).isFalse();
    }

    @Test
    public void whenDeactivateWithExistingRentalsThenThrowException() {
        thrown.expect(IllegalStateException.class);

        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        customer.deactivate();
    }

    @Test
    public void whenDeactivateWithRentalsThenThrowException() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Customer with id 'null' cannot be deactivated while containing rentals.");

        final List<Rental> rentals = RentalDataFixtures.returnedRentals();

        rentals.forEach(r -> customer.addRental(r));

        customer.deactivate();
    }

    @Test
    public void whenCalculateThenReturnCorrectAmount() {
        final List<Rental> rentals = RentalDataFixtures.rentals();

        rentals.forEach(r -> customer.addRental(r));

        final Money result = customer.calculatePrice();

        assertThat(result).isEqualByComparingTo(Money.of(250, result.getCurrency()));
    }

    @Test
    public void givenEndDateNullWhenCalculateExtraChargesThenReturnThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Cannot create late charges if END DATE is not set.");

        final List<Rental> rentals = RentalDataFixtures.rentals();

        rentals.forEach(r -> customer.addRental(r));

        final Money result = customer.calculateExtraCharges();

        assertThat(result).isEqualByComparingTo(Money.of(110, result.getCurrency()));
    }

    @Test
    public void whenRentalsAddedThenRentalsSizeIncreased() {
        final long before = customer.getRentals().size();

        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.forEach(r -> customer.addRental(r));

        final long result = customer.getRentals().size();

        assertThat(result).isEqualByComparingTo(before + rentals.size());
    }

    @Test
    public void whenAddBonusPointsThenBonusPointsAdded() {
        final long before = customer.getBonusPoints();

        final List<Rental> rentals = RentalDataFixtures.rentals();

        rentals.forEach(r -> customer.addRental(r));

        customer.addBonusPoints();

        final long result = customer.getBonusPoints();

        assertThat(result).isEqualByComparingTo(before + 5);
    }

    @Test
    public void whenRentalsReturnedSameDayThenCalculateExtraChargesReturnsZeroAmount() {
        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.forEach(Rental::markReturned);

        rentals.forEach(r -> customer.addRental(r));

        final Money result = customer.calculateExtraCharges();

        assertThat(result).isEqualByComparingTo(Money.of(0, result.getCurrency()));
    }
}
