package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;

import java.math.BigDecimal;

public class InvoiceDataFixtures {

    public static Invoice invoice() {
        return invoice(CustomerDataFixtures.customer());
    }

    public static Invoice invoice(Customer customer) {
        return new Invoice(customer, InvoiceType.UP_FRONT);
    }

    public static String json() {
        return "{\n" +
                "  \"type\": \"UP_FRONT\"\n" +
                "}";
    }
}
