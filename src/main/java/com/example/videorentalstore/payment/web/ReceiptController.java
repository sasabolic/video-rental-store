package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.web.dto.ReceiptResponse;
import com.example.videorentalstore.payment.web.dto.assembler.ReceiptResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST receipt resources.
 */
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final PaymentService paymentService;
    private final ReceiptResponseAssembler receiptResponseAssembler;

    public ReceiptController(PaymentService paymentService, ReceiptResponseAssembler receiptResponseAssembler) {
        this.paymentService = paymentService;
        this.receiptResponseAssembler = receiptResponseAssembler;
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ReceiptResponse> get(@PathVariable("receiptId") long receiptId) {
        final Receipt receipt = paymentService.findById(receiptId);

        return ResponseEntity.ok(receiptResponseAssembler.of(receipt));
    }
}
