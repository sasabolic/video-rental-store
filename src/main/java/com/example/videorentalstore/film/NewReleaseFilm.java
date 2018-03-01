package com.example.videorentalstore.film;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

import static com.example.videorentalstore.film.Price.PREMIUM_PRICE;

@Entity
@DiscriminatorValue("NEW_RELEASE")
public class NewReleaseFilm extends Film {


    public NewReleaseFilm() {

    }

    public NewReleaseFilm(String name) {
        super(name);
    }

    public NewReleaseFilm(String name, int quantity) {
        super(name, quantity);
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
