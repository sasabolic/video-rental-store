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
        final Invoice.Type result = Invoice.Type.fromPathVariable("up-front");

        assertThat(result).isNotNull();
        assertThat(result).isEqualByComparingTo(Invoice.Type.UP_FRONT);
    }

    @Test
    public void givenNonExistingValueWhenFromPathVariableThenThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invoice type of for path variable 'up_front' does not exist.");

        Invoice.Type.fromPathVariable("up_front");
    }
}
