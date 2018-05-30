package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.web.dto.RentalResponse;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link RentalResponseAssembler}.
 *
 * @author Sasa Bolic
 */
@Component
public class DefaultRentalResponseAssembler implements RentalResponseAssembler {

    @Override
    public RentalResponse of(Rental entity) {
        return new RentalResponse(entity.getId(), entity.getFilm().getTitle(), entity.getDaysRented(), entity.getStartDate(), entity.getEndDate());
    }
}
