package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.RentalDataFixtures;

public class CustomerDataFixtures {

    public static Customer customer() {
        return new Customer("John", "Smith");
    }

    public static Customer customerWithRentals(int startedDaysBeforeNow) {
        final Customer customer = customer();
        RentalDataFixtures.rentals(startedDaysBeforeNow).forEach(r -> customer.addRental(r));

        return customer;
    }

    public static Customer customerWithReturnedRentals() {
        final Customer customer = customer();
        RentalDataFixtures.returnedRentals().forEach(r -> customer.addRental(r));

        return customer;
    }
}
