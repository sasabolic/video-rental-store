package com.example.videorentalstore.inventory;

import java.math.BigDecimal;

import static com.example.videorentalstore.inventory.Price.BASIC_PRICE;

public class OldReleaseFilm extends Film {

    public OldReleaseFilm(String name) {
        super(name);
    }

    @Override
    public BigDecimal calculatePrice(long daysRented) {
        if (daysRented <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal price = BASIC_PRICE;

        if (daysRented > 5) {
            price = price.add(BASIC_PRICE.multiply(BigDecimal.valueOf(daysRented - 5)));
        }
        return price;
    }

    @Override
    public int calculateBonusPoints() {
        return 1;
    }
}
