package com.example.videorentalstore.invoice.web.dto;

import com.example.videorentalstore.invoice.web.InvoiceController;
import com.example.videorentalstore.payment.web.PaymentController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Invoice response DTO.
 */
@Getter
public class InvoiceResponse extends ResourceSupport {

    private BigDecimal amount;

    public InvoiceResponse(Long id, BigDecimal amount) {
        this.amount = amount;

        add(linkTo(methodOn(InvoiceController.class).get(id)).withSelfRel());
        add(linkTo(methodOn(PaymentController.class).create(null)).withRel("create_payment"));
    }
}
