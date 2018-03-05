package com.example.videorentalstore.rental;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RentalRepository extends CrudRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.customerId = ?1 AND r.status = ?2")
    Iterable<Rental> findAllByCustomerIdAndStatus(Long customerId, Rental.Status status);
}
