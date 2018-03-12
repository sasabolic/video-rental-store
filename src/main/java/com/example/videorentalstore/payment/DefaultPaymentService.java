package com.example.videorentalstore.payment;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.rental.Rental;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of {@link PaymentService} delegating persistence operations to {@link CustomerRepository} and {@link ReceiptRepository}.
 */
@Service
public class DefaultPaymentService implements PaymentService {

    private final CustomerRepository customerRepository;
    private final ReceiptRepository receiptRepository;

    public DefaultPaymentService(CustomerRepository customerRepository, ReceiptRepository receiptRepository) {
        this.customerRepository = customerRepository;
        this.receiptRepository = receiptRepository;
    }

    @Override
    public List<Receipt> findAllForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        return customer.getReceipts();
    }

    @Override
    public Receipt findById(Long id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new ReceiptNotFoundException(String.format("Receipt with id '%d' does not exist", id)));
    }

    @Override
    public Receipt pay(Long customerId, Invoice.Type type, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));


        switch (type) {
            case UP_FRONT:
                customer.getRentals().stream()
                        .filter(Rental::isUpFrontPaymentExpected)
                        .forEach(Rental::markPaidUpFront);

                customer.addBonusPoints();
                break;
            case LATE_CHARGE:
                customer.getRentals().stream()
                        .filter(Rental::isLatePaymentExpected)
                        .forEach(r -> r.markPayedLate().markCompleted());
        }

        Receipt receipt = new Receipt(amount);
        customer.addReceipt(receipt);

        customerRepository.save(customer);

        return receipt;
    }
}
