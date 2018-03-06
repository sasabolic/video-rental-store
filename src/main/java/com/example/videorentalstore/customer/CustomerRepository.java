package com.example.videorentalstore.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE UPPER(c.firstName) LIKE UPPER(concat('%', ?1,'%')) OR UPPER(c.lastName) LIKE UPPER(concat('%', ?1,'%'))")
    List<Customer> findByNameContainingIgnoreCase(String name);

}
