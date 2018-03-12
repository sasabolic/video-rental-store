package com.example.videorentalstore.invoice.web.dto.assembler;

import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.web.dto.InvoiceResponse;
import org.springframework.stereotype.Component;

/**
 * Assembler for {@link InvoiceResponse} DTO.
 */
@Component
public class DefaultInvoiceResponseAssembler implements InvoiceResponseAssembler {

    @Override
    public InvoiceResponse of(Invoice entity) {
        return new InvoiceResponse(entity.getAmount(), entity.getCustomer(), entity.getType());
    }
}
