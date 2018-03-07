package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalDataFixtures;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {

    private Customer customer = CustomerDataFixtures.customer();

    @Test
    public void whenNewInstanceThenIsActiveReturnsTrue() {
        customer = new Customer("John", "Smith");

        assertThat(customer.isActive()).isTrue();
    }

    @Test
    public void whenDeactivateThenIsActiveReturnsFalse() {
        customer.deactivate();

        assertThat(customer.isActive()).isFalse();
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
    public void whenCalculateThenReturnCorrectAmount() {
        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculate();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.valueOf(250));
    }

    @Test
    public void whenRentalsAddedThenRentalsSizeIncreased() {
        final long before = customer.getRentals().size();

        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        final long result = customer.getRentals().size();

        assertThat(result).isEqualByComparingTo(before + 4);
    }

    @Test
    public void whenRentalsAddedThenBonusPointsAdded() {
        final long before = customer.getBonusPoints();

        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        final long result = customer.getBonusPoints();

        assertThat(result).isEqualByComparingTo(before + 5);
    }

    @Test
    public void whenRentalsReturnedThenBonusPointsNotAdded() {
        final long before = customer.getBonusPoints();

        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.stream().forEach(Rental::markReturned);

        rentals.stream().forEach(r -> customer.addRental(r));

        final long result = customer.getBonusPoints();

        assertThat(result).isEqualByComparingTo(before);
    }

    @Test
    public void whenRentalsReturnedSameDayThenCalculateExtraChargesReturnsZeroAmount() {
        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.stream().forEach(Rental::markReturned);

        rentals.stream().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculateExtraCharges();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void whenRentalsReturnedThenCalculateExtraChargesReturnsCorrectAmount() {
        final List<Rental> rentals = RentalDataFixtures.rentals(3);
        rentals.stream().forEach(Rental::markReturned);

        rentals.stream().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculateExtraCharges();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.valueOf(110));
    }

    @Test
    public void whenRentalsReturnedThenCalculateReturnsCorrectAmount() {
        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.stream().forEach(Rental::markReturned);

        rentals.stream().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculate();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
