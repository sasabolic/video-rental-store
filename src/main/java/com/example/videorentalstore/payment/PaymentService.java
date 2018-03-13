package com.example.videorentalstore.payment;

import com.example.videorentalstore.invoice.Invoice;

import java.util.List;

/**
 * Interface for payment actions.
 */
public interface PaymentService {

    /**
     * Executes payment of {@link Invoice}.
     *
     * @param invoiceId the invoice id
     * @return the receipt
     */
    void pay(Long invoiceId);
}
