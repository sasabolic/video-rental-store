package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultInvoiceService implements InvoiceService {

    private final CustomerRepository customerRepository;

    public DefaultInvoiceService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Invoice calculate(Long customerId, Invoice.Type type) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        switch (type) {
            case UP_FRONT:
                return new Invoice(customer.calculatePrice(), customer, type);
            case LATE_CHARGE:
                return new Invoice(customer.calculateExtraCharges(), customer, type);
            default:
                throw new IllegalArgumentException(String.format("Calculating price for type '%s' is not supported.", type));
        }
    }
}
