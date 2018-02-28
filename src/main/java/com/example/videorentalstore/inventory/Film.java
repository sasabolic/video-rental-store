package com.example.videorentalstore.inventory;

import java.math.BigDecimal;

public abstract class Film {

    private String name;

    public Film(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract BigDecimal calculatePrice(long daysRented);

    public abstract int calculateBonusPoints();

}
