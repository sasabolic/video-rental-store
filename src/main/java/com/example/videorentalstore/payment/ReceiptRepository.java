package com.example.videorentalstore.payment;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface to manage {@code Receipt} instances.
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
