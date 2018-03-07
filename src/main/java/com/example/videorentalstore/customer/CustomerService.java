package com.example.videorentalstore.customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll(String name);

    Customer findById(Long id);

    Customer save(CreateCustomerCommand createFilmCommand);

    Customer update(UpdateCustomerCommand updateFilmCommand);

    Customer delete(Long id);
}