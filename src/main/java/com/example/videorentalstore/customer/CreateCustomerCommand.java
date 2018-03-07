package com.example.videorentalstore.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateCustomerCommand {

    private String firstName;
    private String lastName;
}
