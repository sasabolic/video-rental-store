package com.example.videorentalstore.customer;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Invoice {

    private BigDecimal amount;

    public Invoice(BigDecimal amount) {
        this.amount = amount;
    }
}
