package com.example.videorentalstore.payment;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ReceiptTest {

    private Receipt receipt;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        final Customer customer = spy(CustomerDataFixtures.customer());
        doReturn(12L).when(customer).getId();

        receipt = PaymentDataFixtures.receipt();
    }

    @Test
    public void whenNewInstanceThenFieldsSet() {
        assertThat(receipt.getAmount()).isNotNull();
    }

    @Test
    public void givenAmountNullWhenNewInstanceThenThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Receipt's amount cannot be null!");

        receipt = PaymentDataFixtures.receipt(null);
    }
}
