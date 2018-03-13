package com.example.videorentalstore.invoice.web;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.InvoiceService;
import com.example.videorentalstore.invoice.InvoiceType;
import com.example.videorentalstore.invoice.web.dto.InvoiceResponse;
import com.example.videorentalstore.invoice.web.dto.SaveInvoiceRequest;
import com.example.videorentalstore.invoice.web.dto.assembler.InvoiceResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;


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

    @PostMapping("/customers/{customerId}/invoices")
    public ResponseEntity<Void> create(@PathVariable("customerId") Long customerId, @RequestBody @Valid SaveInvoiceRequest saveInvoiceRequest, HttpServletRequest request) {
        final Invoice invoice = invoiceService.create(customerId, InvoiceType.fromString(saveInvoiceRequest.getType()));

        URI location = ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/invoices/{invoiceId}")
                .buildAndExpand(invoice.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
