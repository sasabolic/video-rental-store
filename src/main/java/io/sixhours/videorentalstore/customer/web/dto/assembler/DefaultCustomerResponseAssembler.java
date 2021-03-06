package io.sixhours.videorentalstore.customer.web.dto.assembler;

import io.sixhours.videorentalstore.customer.Customer;
import io.sixhours.videorentalstore.customer.web.dto.CustomerResponse;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link CustomerResponseAssembler}.
 *
 * @author Sasa Bolic
 */
@Component
public class DefaultCustomerResponseAssembler implements CustomerResponseAssembler {

    @Override
    public CustomerResponse of(Customer entity) {
        return new CustomerResponse(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getBonusPoints());
    }
}
