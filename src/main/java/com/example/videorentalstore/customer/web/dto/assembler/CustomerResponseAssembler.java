package com.example.videorentalstore.customer.web.dto.assembler;

import com.example.videorentalstore.core.GenericResponseAssembler;
import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.web.dto.CustomerResponse;
import org.springframework.hateoas.Resources;

import java.util.Collection;

/**
 * Assembler interface for creating {@link CustomerResponse} DTOs.
 */
public interface CustomerResponseAssembler extends GenericResponseAssembler<Customer, CustomerResponse> {

    Resources<CustomerResponse> of(final Collection<Customer> entities, String name);
}
