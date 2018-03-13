package com.example.videorentalstore.invoice;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.film.FilmDataFixtures;
import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalDataFixtures;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceTest {

    private InvoiceService invoiceService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        invoiceService = new DefaultInvoiceService(customerRepository, invoiceRepository);
    }

    @Test
    public void givenTypeUpFrontWhenCalculatingThenReturnReceipt() {
        final Customer customer = CustomerDataFixtures.customer();
        RentalDataFixtures.rentals().forEach(r -> customer.addRental(r));

        doReturn(Optional.of(customer)).when(customerRepository).findById(anyLong());

        final Invoice result = invoiceService.create(11L, InvoiceType.UP_FRONT);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("amount", BigDecimal.valueOf(250));
    }

    @Test
    public void givenTypeLateChargeWhenCalculatingThenReturnReceipt() {
        final Customer customer = CustomerDataFixtures.customer();

        final Rental newReleaseRental = spy(RentalDataFixtures.rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, 3));
        final Rental regularReleaseRental = spy(RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, 3));
        final Rental regularReleaseRental1 = spy(RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2, 3));
        final Rental oldReleaseRental = spy(RentalDataFixtures.rental(FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7, 3));

        customer.addRental(newReleaseRental.markUpFrontPaymentExpected().markInProcess().markReturned().markLatePaymentExpected());
        customer.addRental(regularReleaseRental.markUpFrontPaymentExpected().markInProcess().markReturned().markLatePaymentExpected());
        customer.addRental(regularReleaseRental1.markUpFrontPaymentExpected().markInProcess().markReturned().markLatePaymentExpected());
        customer.addRental(oldReleaseRental.markUpFrontPaymentExpected().markInProcess().markReturned().markLatePaymentExpected());

        doReturn(Optional.of(customer)).when(customerRepository).findById(anyLong());

        final Invoice result = invoiceService.create(11L, InvoiceType.LATE_CHARGE);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("amount", BigDecimal.valueOf(110));
    }

    @Test
    public void givenNonCustomerWhenCalculatingThenThrowExcpetion() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        invoiceService.create(customerId, InvoiceType.LATE_CHARGE);
    }
}
