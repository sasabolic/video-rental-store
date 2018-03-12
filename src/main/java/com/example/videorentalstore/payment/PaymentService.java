package com.example.videorentalstore.payment;

import com.example.videorentalstore.invoice.Invoice;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for actions on {@code Receipt}.
 */
public interface PaymentService {

    /**
     * Returns list of {@code Receipt} for given {@code customerId}.
     *
     * @param customerId the customer's id
     * @return the list of {@code Receipt} of given customer
     */
    List<Receipt> findAllForCustomer(Long customerId);

    /**
     * Returns {@code Receipt} with given receipt {@code id}.
     *
     * @param id the receipt id
     * @return the receipt
     */
    Receipt findById(Long id);

    /**
     * Executes payment of {@code amount} of selected {@code type} for customer with {@code customerId} and returns {@code Receipt}.
     *
     * @param customerId the customer id
     * @param type       the type of payment
     * @param amount     the amount
     * @return the receipt
     */
    Receipt pay(Long customerId, Invoice.Type type, BigDecimal amount);
}
