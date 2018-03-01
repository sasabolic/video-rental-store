package com.example.videorentalstore.pricing;

import java.math.BigDecimal;

public interface ReleasePolicy {

    BigDecimal calculatePrice(long daysRented);

    int calculateBonusPoints();
}
