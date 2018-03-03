package com.example.videorentalstore.film;

import java.math.BigDecimal;

import static com.example.videorentalstore.film.Price.BASIC_PRICE;
import static com.example.videorentalstore.film.Price.PREMIUM_PRICE;

public enum ReleaseType {

    NEW_RELEASE {
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
    },

    REGULAR_RELEASE {
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
    },

    OLD_RELEASE {
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
    };

    public abstract BigDecimal calculatePrice(long daysRented);

    public abstract int calculateBonusPoints();
}
