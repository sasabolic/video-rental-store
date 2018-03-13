package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultInvoiceService implements InvoiceService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    public DefaultInvoiceService(CustomerRepository customerRepository, InvoiceRepository invoiceRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(String.format("Invoice with id '%d' does not exist", id)));
    }

    @Override
    public Invoice create(Long customerId, InvoiceType type) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        final Invoice invoice = new Invoice(customer, type);

        invoiceRepository.save(invoice);

        return invoice;
    }
}
