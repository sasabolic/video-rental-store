package com.example.videorentalstore.customer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCustomerService implements CustomerService {

    private final CustomerRepository customerRepository;

    public DefaultCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findAll(String name) {
        if (name == null) {
            return this.customerRepository.findAll();
        }
        return this.customerRepository.findByName(name);
    }

    @Override
    public Customer findById(Long id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", id)));
    }

    @Override
    public Customer save(String firstName, String lastName) {
        final Customer customer = new Customer(firstName, lastName);

        return this.customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, String firstName, String lastName) {
        final Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", id)));

        customer.update(firstName, lastName);

        return this.customerRepository.save(customer);
    }

    @Override
    public Customer delete(Long id) {
        final Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", id)));

        customer.deactivate();

        return this.customerRepository.save(customer);
    }
}
