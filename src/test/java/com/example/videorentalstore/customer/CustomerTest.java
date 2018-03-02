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
    public void whenCalculateThenReturnAmountOf250() {
        RentalDataFixtures.rentalList().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculate();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.valueOf(250));
    }

    @Test
    public void givenReturnedRentalsWhenCalculateThenReturnCorrectAmount() {
        final List<Rental> rentals = RentalDataFixtures.rentalList();
        rentals.stream().forEach(Rental::turnBack);

        rentals.stream().forEach(r -> customer.addRental(r));

        final BigDecimal totalAmount = customer.calculateExtraCharges();

        assertThat(totalAmount).isEqualByComparingTo(BigDecimal.valueOf(110));
    }
}
