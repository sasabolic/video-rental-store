package com.example.videorentalstore.payment.web.dto;

import com.example.videorentalstore.payment.web.ReceiptController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.math.BigDecimal;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Receipt response DTO.
 */
@Relation(collectionRelation = "receipts")
@Getter
public class ReceiptResponse extends ResourceSupport {

    private BigDecimal amount;

    public ReceiptResponse(Long id, BigDecimal amount) {
        this.amount = amount;

        add(linkTo(methodOn(ReceiptController.class).get(id)).withSelfRel());
    }
}
