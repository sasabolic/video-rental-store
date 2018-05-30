package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.rental.BatchRental;
import com.example.videorentalstore.rental.web.dto.BatchRentalResponse;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link BatchRentalResponseAssembler}.
 *
 * @author Sasa Bolic
 */
@Component
public class DefaultBatchRentalResponseAssembler implements BatchRentalResponseAssembler {

    private final RentalResponseAssembler rentalResponseAssembler;

    public DefaultBatchRentalResponseAssembler(RentalResponseAssembler rentalResponseAssembler) {
        this.rentalResponseAssembler = rentalResponseAssembler;
    }

    @Override
    public BatchRentalResponse of(BatchRental entity) {
        return new BatchRentalResponse(entity.getAmount(), rentalResponseAssembler.of(entity.getRentals()));
    }
}
