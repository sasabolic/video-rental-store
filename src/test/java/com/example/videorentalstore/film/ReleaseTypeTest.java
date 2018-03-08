package com.example.videorentalstore.film;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseTypeTest {

    @Test
    public void givenNewReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final BigDecimal result = ReleaseType.NEW_RELEASE.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(400));
    }

    @Test
    public void givenNewReleaseWhenCalculatePriceForZeroDaysThenReturnZero() {
        final BigDecimal result = ReleaseType.NEW_RELEASE.calculatePrice(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void givenRegularReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final BigDecimal result = ReleaseType.REGULAR_RELEASE.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(240));
    }

    @Test
    public void givenRegularReleaseWhenCalculatePriceForZeroDaysThenReturnZero() {
        final BigDecimal result = ReleaseType.REGULAR_RELEASE.calculatePrice(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void givenOldReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final BigDecimal result = ReleaseType.OLD_RELEASE.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(180));
    }

    @Test
    public void givenOldReleaseWhenCalculatePriceForZeroDaysThenReturnZero() {
        final BigDecimal result = ReleaseType.OLD_RELEASE.calculatePrice(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void givenNewReleaseWhenCalculateBonusPointsThenReturnCorrectResult() {
        final int result = ReleaseType.NEW_RELEASE.calculateBonusPoints(10);

        assertThat(result).isEqualTo(2);
    }

    @Test
    public void givenNewReleaseWhenCalculateBonusPointsForZeroDaysThenReturnZero() {
        final int result = ReleaseType.NEW_RELEASE.calculateBonusPoints(0);

        assertThat(result).isEqualTo(0);
    }

    @Test
    public void givenRegularReleaseWhenCalculateBonusPointsThenReturnCorrectResult() {
        final int result = ReleaseType.REGULAR_RELEASE.calculateBonusPoints(10);

        assertThat(result).isEqualTo(1);
    }

    @Test
    public void givenRegularReleaseWhenCalculateBonusPointsForZeroDaysThenReturnZero() {
        final int result = ReleaseType.REGULAR_RELEASE.calculateBonusPoints(0);

        assertThat(result).isEqualTo(0);
    }

    @Test
    public void givenOldReleaseWhenCalculateBonusPointsThenReturnCorrectResult() {
        final int result = ReleaseType.OLD_RELEASE.calculateBonusPoints(10);

        assertThat(result).isEqualTo(1);
    }

    @Test
    public void givenOldReleaseWhenCalculateBonusPointsForZeroDaysThenReturnZero() {
        final int result = ReleaseType.OLD_RELEASE.calculateBonusPoints(0);

        assertThat(result).isEqualTo(0);
    }
}
