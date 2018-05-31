package io.sixhours.videorentalstore.customer.web.dto.assembler;

import io.sixhours.videorentalstore.core.GenericResponseAssembler;
import io.sixhours.videorentalstore.customer.Customer;
import io.sixhours.videorentalstore.customer.web.dto.CustomerResponse;

/**
 * Assembler interface for creating {@link CustomerResponse} DTOs.
 *
 * @author Sasa Bolic
 */
public interface CustomerResponseAssembler extends GenericResponseAssembler<Customer, CustomerResponse> {
}
