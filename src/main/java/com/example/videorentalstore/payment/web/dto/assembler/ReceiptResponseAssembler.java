package com.example.videorentalstore.payment.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.web.dto.ReceiptResponse;
import org.springframework.hateoas.Resources;

import java.util.Collection;

/**
 * Assembler interface for creating {@link ReceiptResponse} DTOs.
 */
public interface ReceiptResponseAssembler extends GenericResponseAssembler<Receipt, ReceiptResponse> {

    Resources<ReceiptResponse> of(final Collection<Receipt> entities, Long customerId);
}
