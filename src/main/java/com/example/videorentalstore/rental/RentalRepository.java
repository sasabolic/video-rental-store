package com.example.videorentalstore.rental;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface to manage {@code Rental} instances.
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {
}
