package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.rental.Rental;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public enum InvoiceType {

    UP_FRONT(Rental::isReserved, Rental::markUpFrontPaymentExpected, Rental::markInProcess) {
        @Override
        public BigDecimal calculateAmount(Customer customer) {
            return customer.calculatePrice();
        }
    },

    LATE_CHARGE(Rental::isReturned, Rental::markLatePaymentExpected, Rental::markCompleted) {
        @Override
        public BigDecimal calculateAmount(Customer customer) {
            return customer.calculateExtraCharges();
        }
    };

    private final Predicate<Rental> invoiceFilter;
    private final Function<Rental, Rental> invoiceAction;
    private final Consumer<Rental> paymentAction;

    InvoiceType(Predicate<Rental> invoiceFilter, Function<Rental, Rental> invoiceAction, Consumer<Rental> paymentAction) {
        this.invoiceFilter = invoiceFilter;
        this.invoiceAction = invoiceAction;
        this.paymentAction = paymentAction;
    }

    public abstract BigDecimal calculateAmount(Customer customer);

    public Predicate<Rental> invoiceFilter() {
        return this.invoiceFilter;
    }

    public Function<Rental, Rental> invoceAction() {
        return this.invoiceAction;
    }

    public Consumer<Rental> paymentAction() {
        return this.paymentAction;
    }

    public static InvoiceType fromString(String value) {
        return Arrays.stream(values())
                .filter(e -> e.name().equals(value))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invoice type of '%s' does not exist.", value)));
    }
}