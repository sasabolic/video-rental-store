package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.rental.BatchRental;
import com.example.videorentalstore.rental.web.dto.BatchRentalReponse;

/**
 * Assembler interface for creating {@link BatchRentalReponse} DTOs.
 */
public interface BatchRentalResponseAssembler extends GenericResponseAssembler<BatchRental, BatchRentalReponse> {
}
