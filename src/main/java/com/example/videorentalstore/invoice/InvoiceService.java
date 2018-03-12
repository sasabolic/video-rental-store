package com.example.videorentalstore.invoice;

public interface InvoiceService {

    Invoice calculate(Long customerId, Invoice.Type type);
}
