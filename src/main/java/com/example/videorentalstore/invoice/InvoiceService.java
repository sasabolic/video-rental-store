package com.example.videorentalstore.invoice;

public interface InvoiceService {

    Invoice findById(Long id);

    Invoice create(Long customerId, InvoiceType type);
}
