package com.example.videorentalstore.customer;

import lombok.Getter;

@Getter
public class UpdateCustomerCommand extends CreateCustomerCommand {

    private Long id;

    public UpdateCustomerCommand(Long id, String firstName, String lastName) {
        super(firstName, lastName);
        this.id = id;
    }
}
