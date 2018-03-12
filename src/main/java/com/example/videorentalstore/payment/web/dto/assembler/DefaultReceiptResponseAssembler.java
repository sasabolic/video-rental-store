package com.example.videorentalstore.payment.web.dto.assembler;

import com.example.videorentalstore.customer.web.CustomerController;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.web.CustomerReceiptController;
import com.example.videorentalstore.payment.web.dto.ReceiptResponse;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Implementation of {@link ReceiptResponseAssembler}.
 */
@Component
public class DefaultReceiptResponseAssembler implements ReceiptResponseAssembler {

    @Override
    public ReceiptResponse of(Receipt entity) {
        return new ReceiptResponse(entity.getId(), entity.getAmount());
    }

    @Override
    public Resources<ReceiptResponse> of(Collection<Receipt> entities, long customerId) {
        final Resources<ReceiptResponse> resources = new Resources<>(of(entities));

        resources.add(linkTo(methodOn(CustomerReceiptController.class).getAll(customerId)).withSelfRel());
        resources.add(linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).get(customerId)).withRel("customer"));

        return resources;
    }
}
