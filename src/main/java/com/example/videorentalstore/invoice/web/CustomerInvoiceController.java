package com.example.videorentalstore.invoice.web;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.InvoiceService;
import com.example.videorentalstore.invoice.web.dto.InvoiceResponse;
import com.example.videorentalstore.invoice.web.dto.assembler.InvoiceResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST customer's invoice resources.
 */
@RestController
public class CustomerInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceResponseAssembler invoiceResponseAssembler;

    public CustomerInvoiceController(InvoiceService invoiceService, InvoiceResponseAssembler invoiceResponseAssembler) {
        this.invoiceService = invoiceService;
        this.invoiceResponseAssembler = invoiceResponseAssembler;
    }

    @GetMapping("/customers/{customerId}/invoices/{type}")
    public ResponseEntity<InvoiceResponse> get(@PathVariable("customerId") Long customerId, @PathVariable("type") String type) {
        final Invoice invoice = invoiceService.calculate(customerId, Invoice.Type.fromPathVariable(type));

        return ResponseEntity.ok(invoiceResponseAssembler.of(invoice));
    }

}
