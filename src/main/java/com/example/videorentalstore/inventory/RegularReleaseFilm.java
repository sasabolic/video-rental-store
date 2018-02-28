package com.example.videorentalstore.inventory;

import java.math.BigDecimal;

import static com.example.videorentalstore.inventory.Price.BASIC_PRICE;

public class RegularReleaseFilm extends Film {

    public RegularReleaseFilm(String name) {
        super(name);
    }

    @Override
    public BigDecimal calculatePrice(long daysRented) {
        if (daysRented <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = BASIC_PRICE;

        if (daysRented > 3) {
            price = price.add(BASIC_PRICE.multiply(BigDecimal.valueOf(daysRented - 3)));
        }
        return price;
    }

    @Override
    public int calculateBonusPoints() {
        return 1;
    }
}
