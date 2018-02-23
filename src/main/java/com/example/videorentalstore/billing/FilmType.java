package com.example.videorentalstore.billing;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;

public enum FilmType {

    NEW_RELEASE(Period.of(0, 6, 0), 1, Price.PREMIUM_PRICE, 2),

    REGULAR_FILM(Period.of(3, 0, 0), 3, Price.BASIC_PRICE, 1),

    OLD_FILM(null, 5, Price.BASIC_PRICE, 1);

    private final Period period;
    private final int daysLimit;
    private final Price price;
    private final int points;

    FilmType(Period period, int daysLimit, Price price, int points) {
        this.period = period;
        this.daysLimit = daysLimit;
        this.price = price;
        this.points = points;
    }

    public static FilmType now(Instant dateOfRelease) {
        return onDate(Instant.now(), dateOfRelease);
    }

    public static FilmType onDate(Instant date, Instant dateOfRelease) {
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(dateOfRelease, ZoneId.systemDefault());
        final LocalDateTime onGivenDate = LocalDateTime.ofInstant(date, ZoneId.systemDefault());

        if (onGivenDate.minus(NEW_RELEASE.period).isBefore(localDateTime)) {
            return NEW_RELEASE;
        } else if (onGivenDate.minus(REGULAR_FILM.period).isBefore(localDateTime)) {
            return REGULAR_FILM;
        } else {
            return OLD_FILM;
        }
    }

    public BigDecimal calculate(int days) {
        if (days <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal calculatedPrice = this.price.value();

        int extraDays = days - daysLimit;

        while (extraDays-- > 0) {
            calculatedPrice = calculatedPrice.add(this.price.value());
        }

        return calculatedPrice;
    }
}
