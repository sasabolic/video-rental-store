package com.example.videorentalstore.inventory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

import static com.example.videorentalstore.inventory.Price.BASIC_PRICE;

@Entity
@DiscriminatorValue("OLD_RELEASE")
public class OldReleaseFilm extends Film {

    public OldReleaseFilm() {

    }

    public OldReleaseFilm(String name) {
        super(name);
    }

    public OldReleaseFilm(String name, int quantity) {
        super(name, quantity);
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
