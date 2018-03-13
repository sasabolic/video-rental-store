package com.example.videorentalstore.payment;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.invoice.InvoiceDataFixtures;
import com.example.videorentalstore.invoice.InvoiceNotFoundException;
import com.example.videorentalstore.invoice.InvoiceRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        paymentService = new DefaultPaymentService(customerRepository, invoiceRepository);
    }

    @Test
    public void whenPayThenReturnVoid() {
        final Long invoiceId = 12L;

        doReturn(Optional.of(InvoiceDataFixtures.invoice())).when(invoiceRepository).findById(anyLong());

        paymentService.pay(invoiceId);
    }

    @Test
    public void givenNonInvoiceWhenPayThenThrowException() {
        final long invoiceId = 1L;

        thrown.expect(InvoiceNotFoundException.class);
        thrown.expectMessage("Invoice with id '" + invoiceId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(invoiceRepository).findById(anyLong());

        paymentService.pay(invoiceId);
    }
}
