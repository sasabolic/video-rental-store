package com.example.videorentalstore.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE UPPER(c.firstName) LIKE UPPER(concat('%', ?1,'%')) OR UPPER(c.lastName) LIKE UPPER(concat('%', ?1,'%'))")
    Iterable<Customer> findByNameContainingIgnoreCase(String name);

}
