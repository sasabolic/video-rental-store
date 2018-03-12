package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.web.dto.ReceiptResponse;
import com.example.videorentalstore.payment.web.dto.assembler.ReceiptResponseAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST customer's receipt resources.
 */
@RestController
@RequestMapping("/customers/{customerId}/receipts")
public class CustomerReceiptController {

    private final PaymentService paymentService;
    private final ReceiptResponseAssembler receiptResponseAssembler;

    public CustomerReceiptController(PaymentService paymentService, ReceiptResponseAssembler receiptResponseAssembler) {
        this.paymentService = paymentService;
        this.receiptResponseAssembler = receiptResponseAssembler;
    }

    @GetMapping
    public ResponseEntity<Resources<ReceiptResponse>> getAll(@PathVariable("customerId") Long customerId) {
        final List<Receipt> receipts = paymentService.findAllForCustomer(customerId);

        return ResponseEntity.ok(receiptResponseAssembler.of(receipts, customerId));
    }
}
