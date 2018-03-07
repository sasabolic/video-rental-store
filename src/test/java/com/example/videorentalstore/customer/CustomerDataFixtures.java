package com.example.videorentalstore.customer;

import com.example.videorentalstore.rental.RentalDataFixtures;

import java.util.Arrays;
import java.util.List;

public class CustomerDataFixtures {

    public static List<Customer> customers() {
        return Arrays.asList(
                customer("John", "Smith"),
                customer("Giovanni", "Smith"),
                customer("Evan", "Smith"));
    }

    public static Customer customer() {
        return customer("John", "Smith");
    }

    public static Customer customer(String firstName, String lastName) {
        return new Customer(firstName, lastName);
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
