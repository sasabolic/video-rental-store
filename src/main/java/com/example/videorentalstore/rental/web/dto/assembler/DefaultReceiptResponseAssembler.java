package com.example.videorentalstore.rental.web.dto.assembler;

import com.example.videorentalstore.rental.Receipt;
import com.example.videorentalstore.rental.web.dto.ReceiptResponse;
import org.springframework.stereotype.Component;

@Component
public class DefaultReceiptResponseAssembler implements ReceiptResponseAssembler {

    private final RentalResponseAssembler rentalConverter;

    public DefaultReceiptResponseAssembler(RentalResponseAssembler rentalConverter) {
        this.rentalConverter = rentalConverter;
    }

    @Override
    public ReceiptResponse of(Receipt entity) {
        return new ReceiptResponse(entity.getAmount(), rentalConverter.of(entity.getRentals()));
    }
}
