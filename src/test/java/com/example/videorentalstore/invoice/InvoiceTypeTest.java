package com.example.videorentalstore.invoice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceTypeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenFromValueThenCorrectResult() {
        final InvoiceType result = InvoiceType.fromString("UP_FRONT");

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(InvoiceType.UP_FRONT);
    }

    @Test
    public void givenNonExistingValueWhenFromPathVariableThenThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invoice type of 'non-existing' does not exist.");

        InvoiceType.fromString("non-existing");
    }
}
