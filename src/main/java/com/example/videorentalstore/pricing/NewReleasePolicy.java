package com.example.videorentalstore.pricing;

import java.math.BigDecimal;

import static com.example.videorentalstore.film.Price.PREMIUM_PRICE;

public class NewReleasePolicy implements ReleasePolicy {

    @Override
    public BigDecimal calculatePrice(long daysRented) {
        if (daysRented <= 0) {
            return BigDecimal.ZERO;
        }
        return PREMIUM_PRICE.multiply(BigDecimal.valueOf(daysRented));
    }

    @Override
    public int calculateBonusPoints() {
        return 2;
    }
}
