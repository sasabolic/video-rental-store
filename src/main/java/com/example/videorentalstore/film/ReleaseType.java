package com.example.videorentalstore.film;

import java.math.BigDecimal;

import static com.example.videorentalstore.film.Price.BASIC_PRICE;
import static com.example.videorentalstore.film.Price.PREMIUM_PRICE;

/**
 * Release type of {@link Film} used for calculation of price and bonus points.
 */
public enum ReleaseType {

    /**
     * The New release.
     */
    NEW_RELEASE(PREMIUM_PRICE) {
        @Override
        public BigDecimal calculatePrice(long daysRented) {
            if (daysRented <= 0) {
                return BigDecimal.ZERO;
            }
            return price().multiply(BigDecimal.valueOf(daysRented));
        }

        @Override
        public int calculateBonusPoints(long daysRented) {
            if (daysRented <= 0) {
                return 0;
            }
            return 2;
        }
    },

    /**
     * The Regular release.
     */
    REGULAR_RELEASE(BASIC_PRICE) {
        @Override
        public BigDecimal calculatePrice(long daysRented) {
            if (daysRented <= 0) {
                return BigDecimal.ZERO;
            }

            BigDecimal price = price();

            if (daysRented > 3) {
                price = price.add(price().multiply(BigDecimal.valueOf(daysRented - 3)));
            }
            return price;
        }

        @Override
        public int calculateBonusPoints(long daysRented) {
            if (daysRented <= 0) {
                return 0;
            }
            return 1;
        }
    },

    /**
     * The Old release.
     */
    OLD_RELEASE(BASIC_PRICE) {
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
        public int calculateBonusPoints(long daysRented) {
            if (daysRented <= 0) {
                return 0;
            }
            return 1;
        }
    };

    private final BigDecimal price;

    ReleaseType(BigDecimal price) {
        this.price = price;
    }

    BigDecimal price() {
        return this.price;
    }

    /**
     * Calculates price.
     *
     * @param daysRented the days rented
     * @return the big decimal
     */
    public abstract BigDecimal calculatePrice(long daysRented);

    /**
     * Calculates extra charges.
     *
     * @param extraDays the extra days
     * @return the big decimal
     */
    public BigDecimal calculateExtraCharges(long extraDays) {
        return price().multiply(BigDecimal.valueOf(extraDays));
    }

    /**
     * Calculates bonus points.
     *
     * @param daysRented the days rented
     * @return the int
     */
    public abstract int calculateBonusPoints(long daysRented);
}
