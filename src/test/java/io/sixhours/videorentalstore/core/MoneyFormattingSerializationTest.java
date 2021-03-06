package io.sixhours.videorentalstore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyFormattingSerializationTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper().registerModules(Collections.singletonList(new JsonConfig.MoneyModule()));
    }

    @Test
    public void whenWriteAsStringThenCorrectResult() throws JsonProcessingException {

        String result = objectMapper.writeValueAsString(Money.of(12.99, "RSD"));

        assertThat(result).isEqualTo("\"RSD 12.99\"");
    }

    @Test
    public void whenReadThenCorrectResult() throws IOException {

        final MonetaryAmount result = objectMapper.readValue("\"RSD 12.99\"", MonetaryAmount.class);

        assertThat(result).isEqualTo(Money.of(12.99, "RSD"));
    }

    @Test
    public void givenRootLocaleWhenFormatThenCorrectResult() {
        final MonetaryAmountFormat format = MonetaryFormats.getAmountFormat(
                AmountFormatQueryBuilder.of(Locale.ROOT).build());

        final String result = format.format(Money.of(250, "RSD"));

        final String expected = "RSD 250.00";

        for (int i = 0; i < result.length(); i++) {
            System.out.println("first: '" + result.codePointAt(i) + "' second: '" + expected.codePointAt(i) + "'");
        }

        assertThat(result).isEqualTo(expected);
    }
}
