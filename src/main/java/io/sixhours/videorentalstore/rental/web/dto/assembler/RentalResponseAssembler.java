package io.sixhours.videorentalstore.rental.web.dto.assembler;

import io.sixhours.videorentalstore.core.GenericResponseAssembler;
import io.sixhours.videorentalstore.rental.Rental;
import io.sixhours.videorentalstore.rental.web.dto.RentalResponse;

/**
 * Assembler interface for creating {@link RentalResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface RentalResponseAssembler extends GenericResponseAssembler<Rental, RentalResponse> {
}
