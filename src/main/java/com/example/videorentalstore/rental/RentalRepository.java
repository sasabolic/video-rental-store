package com.example.videorentalstore.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface to manage {@code Rental} instances.
 */
@Transactional(readOnly = true)
public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r WHERE r.active = true")
    @Override
    List<Rental> findAll();

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.active = true")
    @Override
    long count();
}
