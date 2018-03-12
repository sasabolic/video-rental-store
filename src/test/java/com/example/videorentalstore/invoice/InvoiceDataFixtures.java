package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;

import java.math.BigDecimal;

public class InvoiceDataFixtures {

    public static Invoice invoice() {
        return invoice(BigDecimal.TEN);
    }

    public static Invoice invoice(BigDecimal value) {
        return invoice(value, CustomerDataFixtures.customer());
    }

    public static Invoice invoice(BigDecimal value, Customer customer) {
        return new Invoice(value, customer, Invoice.Type.UP_FRONT);
    }
}
