package com.example.videorentalstore.pricing;

import java.math.BigDecimal;

public enum ReleasePolicy {

    NEW_RELEASE {
        
    },

    REGULAR_RELEASE,

    OLD_RELEASE;

    public abstract BigDecimal calculatePrice(long daysRented);

    public abstract int calculateBonusPoints();
}
