package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.rental.BatchRental;
import com.example.videorentalstore.rental.web.dto.BatchRentalResponse;

/**
 * Assembler interface for creating {@link BatchRentalResponse} DTOs.
 */
public interface BatchRentalResponseAssembler extends GenericResponseAssembler<BatchRental, BatchRentalResponse> {
}
