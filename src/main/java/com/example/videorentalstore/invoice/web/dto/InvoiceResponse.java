package com.example.videorentalstore.invoice.web.dto;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.web.CustomerInvoiceController;
import com.example.videorentalstore.payment.web.CustomerPaymentController;
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

    public InvoiceResponse(BigDecimal amount, Customer customer, Invoice.Type type) {
        this.amount = amount;

        add(linkTo(methodOn(CustomerPaymentController.class).getAll(customer.getId())).withRel("payments"));
        add(linkTo(methodOn(CustomerInvoiceController.class).get(customer.getId(), type.pathVariable())).withSelfRel());
    }
}
