package com.example.videorentalstore.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Modifying
    @Query("UPDATE Customer c SET c.active = false WHERE c.id = ?1")
    void deactivate(Long id);
}
