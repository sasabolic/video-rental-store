package com.example.videorentalstore.payment;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.invoice.Invoice;
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
    private ReceiptRepository receiptRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        paymentService = new DefaultPaymentService(customerRepository, receiptRepository);
    }

    @Test
    public void whenFindingAllForCustomerThenReturnListOfReceipts() {
        doReturn(Optional.of(CustomerDataFixtures.customerWithReceipts())).when(customerRepository).findById(anyLong());

        final List<Receipt> result = paymentService.findAllForCustomer(1L);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }

    @Test
    public void whenFindingAllForNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        paymentService.findAllForCustomer(customerId);
    }

    @Test
    public void whenFindingByIdThenReturnReceipt() {
        doReturn(Optional.of(PaymentDataFixtures.receipt(BigDecimal.TEN))).when(receiptRepository).findById(isA(Long.class));

        final Receipt result = paymentService.findById(4L);

        assertThat(result).isNotNull();
    }

    @Test
    public void whenFindByNonExistingIdThenThrowException() {
        final long receiptId = 1L;

        thrown.expect(ReceiptNotFoundException.class);
        thrown.expectMessage("Receipt with id '" + receiptId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(receiptRepository).findById(isA(Long.class));

        paymentService.findById(receiptId);
    }

    @Test
    public void givenTypeUpFrontWhenCreatingThenReturnReceipt() {
        final Long customerId = 12L;
        final BigDecimal amount = BigDecimal.TEN;

        doReturn(Optional.of(CustomerDataFixtures.customerWithRentals(0))).when(customerRepository).findById(anyLong());

        final Receipt result = paymentService.pay(customerId, Invoice.Type.UP_FRONT, amount);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("amount", amount);
    }

    @Test
    public void givenTypeLateChargeWhenCalculatingThenReturnReceipt() {
        final Long customerId = 12L;
        final BigDecimal amount = BigDecimal.ONE;
        final Customer customer = CustomerDataFixtures.customerWithRentals(3);

        customer.getRentals().stream().forEach(r -> {
            r.markPaidUpFront().markInProcess().markReturned().markLatePaymentExpected();
        });

        doReturn(Optional.of(customer)).when(customerRepository).findById(anyLong());

        final Receipt result = paymentService.pay(customerId, Invoice.Type.LATE_CHARGE, amount);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("amount", amount);
    }

    @Test
    public void givenNonCustomerWhenCalculatingThenThrowExcpetion() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        paymentService.pay(customerId, Invoice.Type.LATE_CHARGE, BigDecimal.TEN);
    }
}
