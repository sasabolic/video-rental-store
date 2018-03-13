package com.example.videorentalstore.invoice.web.dto.assembler;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.web.dto.InvoiceResponse;
import com.example.videorentalstore.rental.web.dto.RentalResponse;
import com.example.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link InvoiceResponseAssembler}.
 */
@Component
public class DefaultInvoiceResponseAssembler implements InvoiceResponseAssembler {

    private final RentalResponseAssembler rentalResponseAssembler;

    public DefaultInvoiceResponseAssembler(RentalResponseAssembler rentalResponseAssembler) {
        this.rentalResponseAssembler = rentalResponseAssembler;
    }

    @Override
    public InvoiceResponse of(Invoice entity) {
        return new InvoiceResponse(entity.getId(), entity.getAmount());
    }
}
