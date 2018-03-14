package com.example.videorentalstore.film;

import org.javamoney.moneta.Money;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ReleaseTypeTest {

    @Test
    public void givenNewReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final Money result = ReleaseType.NEW_RELEASE.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(400, result.getCurrency()));
    }

    @Test
    public void givenNewReleaseWhenCalculatePriceForZeroDaysThenReturnZero() {
        final Money result = ReleaseType.NEW_RELEASE.calculatePrice(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(0, result.getCurrency()));
    }

    @Test
    public void givenRegularReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final Money result = ReleaseType.REGULAR_RELEASE.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(240, result.getCurrency()));
    }

    @Test
    public void givenRegularReleaseWhenCalculatePriceForZeroDaysThenReturnZero() {
        final Money result = ReleaseType.REGULAR_RELEASE.calculatePrice(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(0, result.getCurrency()));
    }

    @Test
    public void givenOldReleaseWhenCalculatePriceThenReturnCorrectResult() {
        final Money result = ReleaseType.OLD_RELEASE.calculatePrice(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(180, result.getCurrency()));
    }

    @Test
    public void givenNewReleaseWhenCalculateExtraChargesThenReturnCorrectResult() {
        final Money result = ReleaseType.NEW_RELEASE.calculateExtraCharges(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(400, result.getCurrency()));
    }

    @Test
    public void givenNewReleaseWhenCalculateExtraChargesForZeroDaysThenReturnZero() {
        final Money result = ReleaseType.NEW_RELEASE.calculateExtraCharges(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(0, result.getCurrency()));
    }

    @Test
    public void givenRegularReleaseWhenCalculateExtraChargesThenReturnCorrectResult() {
        final Money result = ReleaseType.REGULAR_RELEASE.calculateExtraCharges(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(300, result.getCurrency()));
    }

    @Test
    public void givenRegularReleaseWhenCalculateExtraChargesForZeroDaysThenReturnZero() {
        final Money result = ReleaseType.REGULAR_RELEASE.calculateExtraCharges(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(0, result.getCurrency()));
    }

    @Test
    public void givenOldReleaseWhenCalculateExtraChargesThenReturnCorrectResult() {
        final Money result = ReleaseType.OLD_RELEASE.calculateExtraCharges(10);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(300, result.getCurrency()));
    }

    @Test
    public void givenOldReleaseWhenCalculateExtraChargesForZeroDaysThenReturnZero() {
        final Money result = ReleaseType.OLD_RELEASE.calculateExtraCharges(0);

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Money.of(0, result.getCurrency()));
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
