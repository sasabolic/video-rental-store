package com.example.videorentalstore.customer;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
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
        return this.customerRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Customer findById(Long id) {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", id)));
    }

    @Override
    public Customer save(CreateCustomerCommand createCustomerCommand) {
        final Customer customer = new Customer(createCustomerCommand.getFirstName(), createCustomerCommand.getLastName());

        return this.customerRepository.save(customer);
    }

    @Override
    public Customer update(UpdateCustomerCommand updateCustomerCommand) {
        final Customer customer = this.customerRepository.findById(updateCustomerCommand.getId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", updateCustomerCommand.getId())));

        customer.update(updateCustomerCommand.getFirstName(), updateCustomerCommand.getLastName());

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
