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
    public void whenCalculateThenReturnCorrectAmount() {
        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculate();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.valueOf(250));
    }

    @Test
    public void whenRentalsAddedThenBonusPointsAreAdded() {
        final long before = customer.getBonusPoints();

        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        final long result = customer.getBonusPoints();

        assertThat(result).isEqualByComparingTo(before + 5);
    }

    @Test
    public void whenRentalsReturnedThenBonusPointsAreNotAdded() {
        final long before = customer.getBonusPoints();

        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.stream().forEach(Rental::markReturned);

        rentals.stream().forEach(r -> customer.addRental(r));

        final long result = customer.getBonusPoints();

        assertThat(result).isEqualByComparingTo(before);
    }

    @Test
    public void whenRentalsReturnedImmediatelyThenCalculateExtraChargesReturnsZero() {
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
