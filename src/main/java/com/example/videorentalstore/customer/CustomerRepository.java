package com.example.videorentalstore.customer;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

//    List<Customer> findById(String lastName);
}
