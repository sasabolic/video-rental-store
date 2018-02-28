package com.example.videorentalstore.inventory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

import static com.example.videorentalstore.inventory.Price.PREMIUM_PRICE;

@Entity
@DiscriminatorValue("NEW_RELEASE")
public class NewReleaseFilm extends Film {

    public NewReleaseFilm(String name) {
        super(name);
    }

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
