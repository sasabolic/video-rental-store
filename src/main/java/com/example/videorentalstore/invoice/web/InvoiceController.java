package com.example.videorentalstore.invoice.web;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.InvoiceService;
import com.example.videorentalstore.invoice.web.dto.InvoiceResponse;
import com.example.videorentalstore.invoice.web.dto.assembler.InvoiceResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST customer's invoice resources.
 */
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceResponseAssembler invoiceResponseAssembler;

    public InvoiceController(InvoiceService invoiceService, InvoiceResponseAssembler invoiceResponseAssembler) {
        this.invoiceService = invoiceService;
        this.invoiceResponseAssembler = invoiceResponseAssembler;
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> get(@PathVariable("invoiceId") Long invoiceId) {
        final Invoice invoice = invoiceService.findById(invoiceId);

        return ResponseEntity.ok(invoiceResponseAssembler.of(invoice));
    }
}
