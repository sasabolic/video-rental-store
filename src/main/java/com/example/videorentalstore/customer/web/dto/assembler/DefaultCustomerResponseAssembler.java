package com.example.videorentalstore.customer.web.dto.assembler;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.web.CustomerController;
import com.example.videorentalstore.customer.web.dto.CustomerResponse;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Implementation of {@link CustomerResponseAssembler}.
 */
@Component
public class DefaultCustomerResponseAssembler implements CustomerResponseAssembler {

    @Override
    public CustomerResponse of(Customer entity) {
        return new CustomerResponse(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getBonusPoints());
    }

    @Override
    public Resources<CustomerResponse> of(Collection<Customer> entities, String name) {
        final Resources<CustomerResponse> resources = new Resources<>(of(entities));

        resources.add(linkTo(methodOn(CustomerController.class).getAll(name)).withSelfRel());

        return resources;
    }
}
