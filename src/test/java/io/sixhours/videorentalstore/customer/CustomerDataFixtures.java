package io.sixhours.videorentalstore.customer;

import io.sixhours.videorentalstore.rental.RentalDataFixtures;

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
        RentalDataFixtures.rentals(startedDaysBeforeNow).forEach(customer::addRental);

        return customer;
    }

    public static String json() {
        return "{\n" +
                "  \"first_name\": \"John\",\n" +
                "  \"last_name\": \"Smith\"\n" +
                "}";
    }
}
