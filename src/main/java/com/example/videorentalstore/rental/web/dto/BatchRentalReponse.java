package com.example.videorentalstore.rental.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.javamoney.moneta.Money;

import java.util.List;

@AllArgsConstructor
@Getter
public class BatchRentalReponse {

    private Money amount;
    private List<RentalResponse> rentals;
}
