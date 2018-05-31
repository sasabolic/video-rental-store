package io.sixhours.videorentalstore.rental.web.dto.assembler;

import io.sixhours.videorentalstore.core.GenericResponseAssembler;
import io.sixhours.videorentalstore.rental.BatchRental;
import io.sixhours.videorentalstore.rental.web.dto.BatchRentalResponse;

/**
 * Assembler interface for creating {@link BatchRentalResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface BatchRentalResponseAssembler extends GenericResponseAssembler<BatchRental, BatchRentalResponse> {
}
