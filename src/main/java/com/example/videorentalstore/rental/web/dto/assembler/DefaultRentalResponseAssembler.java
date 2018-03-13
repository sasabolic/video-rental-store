package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.web.CustomerInvoiceController;
import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.web.CustomerRentalController;
import com.example.videorentalstore.rental.web.dto.RentalResponse;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

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
    public Resources<RentalResponse> of(Collection<Rental> entities, Rental.Status status, Long customerId) {
        final Resources<RentalResponse> resources = new Resources<>(of(entities));


        resources.add(linkTo(methodOn(CustomerRentalController.class).getAll(customerId, status != null ? status.name() : null)).withSelfRel());

        final Optional<Rental> invoiceableRental = entities.stream().filter(e -> e.isReserved() || e.isReturned()).findAny();
        if (invoiceableRental.isPresent()) {
            resources.add(linkTo(methodOn(CustomerInvoiceController.class).create(customerId, null, null)).withRel("create_invoice"));
        }

        return resources;
    }
}
