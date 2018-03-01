package com.example.videorentalstore.film;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

import static com.example.videorentalstore.film.Price.BASIC_PRICE;

@Entity
@DiscriminatorValue("REGULAR_RELEASE")
public class RegularReleaseFilm extends Film {


    public RegularReleaseFilm() {

    }

    public RegularReleaseFilm(String name) {
        super(name);
    }

    public RegularReleaseFilm(String name, int quantity) {
        super(name, quantity);
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
