package com.example.videorentalstore.rental;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ReturnRentalsCommand {

    private Long customerId;
    private List<Long> rentalIds;
}
