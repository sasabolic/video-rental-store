package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.web.CustomerInvoiceController;
import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.web.CustomerRentalController;
import com.example.videorentalstore.rental.web.dto.RentalResponse;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Implementation of {@link RentalResponseAssembler}.
 */
@Component
public class DefaultRentalResponseAssembler implements RentalResponseAssembler {

    @Override
    public RentalResponse of(Rental entity) {
        return new RentalResponse(entity.getId(), entity.getFilm().getTitle(), entity.getDaysRented(), entity.getStatus().name(), entity.getStartDate(), entity.getReturnDate());
    }

    @Override
    public Resources<RentalResponse> of(Collection<Rental> entities, Rental.Status status, long customerId) {
        final Resources<RentalResponse> resources = new Resources<>(of(entities));

        String type = null;
        if (Rental.Status.UP_FRONT_PAYMENT_EXPECTED.equals(status)) {
            type = Invoice.Type.UP_FRONT.pathVariable();
        } else if (Rental.Status.LATE_PAYMENT_EXPECTED.equals(status)) {
            type = Invoice.Type.LATE_CHARGE.pathVariable();
        }

        resources.add(linkTo(methodOn(CustomerRentalController.class).getAll(customerId, status != null ? status.name() : null)).withSelfRel());
        resources.add(linkTo(methodOn(CustomerInvoiceController.class).get(customerId, type)).withRel("invoices"));

        return resources;
    }
}
