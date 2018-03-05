package com.example.videorentalstore.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.customerId = ?1 AND r.status = ?2")
    List<Rental> findAllByCustomerIdAndStatus(Long customerId, Rental.Status status);
}
