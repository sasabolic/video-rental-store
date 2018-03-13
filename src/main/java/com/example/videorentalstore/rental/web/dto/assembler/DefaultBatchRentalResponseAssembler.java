package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.rental.BatchRental;
import com.example.videorentalstore.rental.web.dto.BatchRentalReponse;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link BatchRentalResponseAssembler}.
 */
@Component
public class DefaultBatchRentalResponseAssembler implements BatchRentalResponseAssembler {

    private final RentalResponseAssembler rentalResponseAssembler;

    public DefaultBatchRentalResponseAssembler(RentalResponseAssembler rentalResponseAssembler) {
        this.rentalResponseAssembler = rentalResponseAssembler;
    }

    @Override
    public BatchRentalReponse of(BatchRental entity) {
        return new BatchRentalReponse(entity.getAmount(), rentalResponseAssembler.of(entity.getRentals()));
    }
}
