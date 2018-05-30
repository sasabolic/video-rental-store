package com.example.videorentalstore.customer.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.web.dto.CustomerResponse;

/**
 * Assembler interface for creating {@link CustomerResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface CustomerResponseAssembler extends GenericResponseAssembler<Customer, CustomerResponse> {
}
