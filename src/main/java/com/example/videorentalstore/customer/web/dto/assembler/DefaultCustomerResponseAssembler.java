package com.example.videorentalstore.customer.web.dto.assembler;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.web.dto.CustomerResponse;

public class DefaultCustomerResponseAssembler implements CustomerResponseAssembler {

    @Override
    public CustomerResponse of(Customer entity) {
        return new CustomerResponse(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getBonusPoints());
    }
}
