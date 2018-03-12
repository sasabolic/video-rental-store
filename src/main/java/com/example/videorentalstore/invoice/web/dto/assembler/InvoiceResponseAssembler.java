package com.example.videorentalstore.invoice.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.web.dto.InvoiceResponse;

/**
 * Assembler interface for creating {@link InvoiceResponse} DTOs.
 */
public interface InvoiceResponseAssembler extends GenericResponseAssembler<Invoice, InvoiceResponse> {
}
