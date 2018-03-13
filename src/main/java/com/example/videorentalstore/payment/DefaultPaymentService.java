package com.example.videorentalstore.payment;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.InvoiceNotFoundException;
import com.example.videorentalstore.invoice.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link PaymentService} delegating persistence operations to {@link CustomerRepository} and {@link ReceiptRepository}.
 */
@Service
@Transactional
public class DefaultPaymentService implements PaymentService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    public DefaultPaymentService(CustomerRepository customerRepository, InvoiceRepository invoiceRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void pay(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(String.format("Invoice with id '%d' does not exist", invoiceId)));

        invoice.pay();

        invoiceRepository.save(invoice.deactivate());

        final Customer customer = invoice.getCustomer();
        customer.addBonusPoints();

        customerRepository.save(customer);
    }
}
