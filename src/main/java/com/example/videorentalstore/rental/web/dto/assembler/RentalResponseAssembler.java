package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.web.dto.RentalResponse;

/**
 * Assembler interface for creating {@link RentalResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface RentalResponseAssembler extends GenericResponseAssembler<Rental, RentalResponse> {
}
