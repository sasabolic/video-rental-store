package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.web.dto.RentalResponse;
import org.springframework.hateoas.Resources;

import java.util.Collection;
import java.util.List;

public interface RentalResponseAssembler extends GenericResponseAssembler<Rental, RentalResponse> {

    Resources<RentalResponse> of(final Collection<Rental> entities, Rental.Status status, long customerId);
}
