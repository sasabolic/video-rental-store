package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.web.dto.SavePaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;


/**
 * REST endpoint for executing payments for customers.
 */
@RestController
@RequestMapping("/customers/{customerId}/payments")
public class CustomerPaymentController {

    private final PaymentService paymentService;

    public CustomerPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable("customerId") Long customerId, @RequestBody @Valid SavePaymentRequest savePaymentRequest, HttpServletRequest httpServletRequest) {
        final Receipt receipt = paymentService.pay(customerId, Invoice.Type.valueOf(savePaymentRequest.getType()), new BigDecimal(savePaymentRequest.getAmount()));

        URI location = ServletUriComponentsBuilder
                .fromContextPath(httpServletRequest)
                .path("/receipts/{receiptId}")
                .buildAndExpand(receipt.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Void> getAll(@PathVariable("customerId") Long customerId) {
        throw new UnsupportedOperationException("HTTP method GET is not supported by this URL");
    }

}
