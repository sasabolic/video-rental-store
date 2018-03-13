package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.web.dto.SavePaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST endpoint for executing payments.
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid SavePaymentRequest savePaymentRequest) {
        paymentService.pay(savePaymentRequest.getInvoiceId());

        return ResponseEntity.noContent().build();
    }
}