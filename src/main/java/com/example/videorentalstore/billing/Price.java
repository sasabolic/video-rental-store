package com.example.videorentalstore.billing;

import java.math.BigDecimal;

public enum Price {

    PREMIUM_PRICE(BigDecimal.valueOf(40)),

    BASIC_PRICE(BigDecimal.valueOf(30));

    private final BigDecimal value;

    Price(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal value() {
        return this.value;
    }
}
