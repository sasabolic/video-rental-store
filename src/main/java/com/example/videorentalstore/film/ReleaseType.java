package com.example.videorentalstore.film;

import org.javamoney.moneta.Money;

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
        public Money calculatePrice(long daysRented) {
            if (daysRented <= 0) {
                return Money.of(0, price().getCurrency());
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
        public Money calculatePrice(long daysRented) {
            if (daysRented <= 0) {
                return Money.of(0, price().getCurrency());
            }

            Money amount = price();

            if (daysRented > 3) {
                amount = amount.add(price().multiply(BigDecimal.valueOf(daysRented - 3)));
            }
            return amount;
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
        public Money calculatePrice(long daysRented) {
            if (daysRented <= 0) {
                return Money.of(0, price().getCurrency());
            }

            Money amount = BASIC_PRICE;

            if (daysRented > 5) {
                amount = amount.add(BASIC_PRICE.multiply(BigDecimal.valueOf(daysRented - 5)));
            }
            return amount;
        }

        @Override
        public int calculateBonusPoints(long daysRented) {
            if (daysRented <= 0) {
                return 0;
            }
            return 1;
        }
    };

    private final Money price;

    ReleaseType(Money price) {
        this.price = price;
    }

    /**
     * Price money.
     *
     * @return the money
     */
    Money price() {
        return this.price;
    }

    /**
     * Calculates price.
     *
     * @param daysRented the days rented
     * @return money
     */
    public abstract Money calculatePrice(long daysRented);

    /**
     * Calculates extra charges.
     *
     * @param extraDays the extra days
     * @return the money
     */
    public Money calculateExtraCharges(long extraDays) {
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
