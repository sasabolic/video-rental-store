package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CreateRentalsCommand {

    private Long customerId;
    private List<CreateRentalCommand> createRentalCommands;
}
