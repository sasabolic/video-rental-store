package io.sixhours.videorentalstore.film;

import org.javamoney.moneta.Money;


/**
 * The interface containing {@link ReleaseType} price information.
 *
 * @author Sasa Bolic
 */
public interface Price {

    String CURRENCY_CODE = "SEK";

    Money PREMIUM_PRICE = Money.of(40, CURRENCY_CODE);

    Money BASIC_PRICE = Money.of(30, CURRENCY_CODE);

}
