package com.example.videorentalstore.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface to manage {@code Invoice} instances.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
