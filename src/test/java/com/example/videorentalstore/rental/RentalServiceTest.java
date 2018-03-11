package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.film.FilmDataFixtures;
import com.example.videorentalstore.film.FilmRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class RentalServiceTest {

    private RentalService rentalService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        rentalService = new DefaultRentalService(customerRepository, filmRepository, rentalRepository);
    }

    @Test
    public void whenFindingAllThenReturnListOfRentals() {
        doReturn(RentalDataFixtures.rentals()).when(rentalRepository).findAll();

        final List<Rental> result = rentalService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }

    @Test
    public void whenFindingAllForCustomerThenReturnListOfRentals() {
        doReturn(Optional.of(CustomerDataFixtures.customerWithRentals(3))).when(customerRepository).findById(anyLong());

        final List<Rental> result = rentalService.findAllForCustomer(1L, null);

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

        rentalService.findAllForCustomer(1L, null);
    }

    @Test
    public void whenFindingAllForCustomerWithStatusThenReturnListOfRentals() {
        doReturn(Optional.of(CustomerDataFixtures.customerWithRentals(3))).when(customerRepository).findById(anyLong());

        final List<Rental> result = rentalService.findAllForCustomer(1L, Rental.Status.UP_FRONT_PAYMENT_EXPECTED);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
    }

    @Test
    public void whenCreatingRentalsThenReturnListOfRentals() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        doReturn(Optional.of(FilmDataFixtures.newReleaseFilm("Matrix 11"))).when(filmRepository).findById(eq(1L));
        doReturn(Optional.of(FilmDataFixtures.regularReleaseFilm("Spider Man"))).when(filmRepository).findById(eq(2L));
        doReturn(Optional.of(FilmDataFixtures.regularReleaseFilm("Spider Man 2"))).when(filmRepository).findById(eq(3L));
        doReturn(Optional.of(FilmDataFixtures.oldReleaseFilm("Out of Africa"))).when(filmRepository).findById(eq(4L));

        final List<RentalInfo> rentalInfos = Arrays.asList(
                new RentalInfo(1L, 1),
                new RentalInfo(2L, 5),
                new RentalInfo(3L, 2),
                new RentalInfo(4L, 7));

        final List<Rental> result = rentalService.create(1L, rentalInfos);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).extracting(r -> r.getFilm().getTitle()).containsExactly("Matrix 11", "Spider Man", "Spider Man 2", "Out of Africa");
        assertThat(result).extracting(r -> r.getDaysRented()).containsExactly(1, 5, 2, 7);
    }

    @Test
    public void whenCreatingRentalsForNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        final List<RentalInfo> rentalInfos = Arrays.asList(
                new RentalInfo(1L, 1),
                new RentalInfo(2L, 5),
                new RentalInfo(3L, 2),
                new RentalInfo(4L, 7));

        rentalService.create(1L, rentalInfos);
    }

    @Test
    public void whenCreatingRentalsForNonExistingFilmsThenThrowException() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(anyLong());

        final List<RentalInfo> rentalInfos = Arrays.asList(
                new RentalInfo(1L, 1),
                new RentalInfo(2L, 5),
                new RentalInfo(3L, 2),
                new RentalInfo(4L, 7));

        List<Rental> result = null;
        try {
            result = rentalService.create(1L, rentalInfos);
        } catch (RentalException ex) {
            assertThat(ex).isNotNull();
            assertThat(ex.isEmpty()).isFalse();
            assertThat(ex.getExceptions()).hasSize(4);
            assertThat(ex.getExceptions()).extracting(Exception::getMessage)
                    .containsExactly(
                            "Film with id '1' does not exist",
                            "Film with id '2' does not exist",
                            "Film with id '3' does not exist",
                            "Film with id '4' does not exist"
                    );
        }

        assertThat(result).isNull();
    }

    @Test
    public void whenReturningBackRentalsThenReturnListOfRentals() {
        final Customer customer = CustomerDataFixtures.customer();

        final Rental newReleaseRental = spy(RentalDataFixtures.rental(FilmDataFixtures.newReleaseFilm("Matrix 11"), 1, 3));
        final Rental regularReleaseRental = spy(RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man"), 5, 3));
        final Rental regularReleaseRental1 = spy(RentalDataFixtures.rental(FilmDataFixtures.regularReleaseFilm("Spider Man 2"), 2, 3));
        final Rental oldReleaseRental = spy(RentalDataFixtures.rental(FilmDataFixtures.oldReleaseFilm("Out of Africa"), 7, 3));

        customer.addRental(newReleaseRental.markPaidUpFront().markInProcess());
        customer.addRental(regularReleaseRental.markPaidUpFront().markInProcess());
        customer.addRental(regularReleaseRental1.markPaidUpFront().markInProcess());
        customer.addRental(oldReleaseRental.markPaidUpFront().markInProcess());

        doReturn(Optional.of(customer)).when(customerRepository).findById(anyLong());
        doReturn(1L).when(newReleaseRental).getId();
        doReturn(2L).when(regularReleaseRental).getId();
        doReturn(3L).when(regularReleaseRental1).getId();
        doReturn(4L).when(oldReleaseRental).getId();

        final List<Rental> result = rentalService.returnBack(1L, Arrays.asList(1L, 2L, 3L, 4L));

        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).extracting(r -> r.getFilm().getTitle()).containsExactly("Matrix 11", "Spider Man", "Spider Man 2", "Out of Africa");
        assertThat(result).extracting(r -> r.getReturnDate()).isNotNull();
    }

    @Test
    public void whenReturningBackRentalsOfNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        rentalService.returnBack(customerId, Arrays.asList(1L, 2L, 3L, 4L));
    }

    @Test
    public void whenReturningBackRentalsOfNonExistingFilmsThenThrowException() {
        final long customerId = 1L;
        final Customer customer = spy(CustomerDataFixtures.customer());

        doReturn(customerId).when(customer).getId();
        doReturn(Optional.of(customer)).when(customerRepository).findById(anyLong());

        List<Rental> result = null;
        try {
            result = rentalService.returnBack(customerId, Arrays.asList(1L, 2L, 3L, 4L));
        } catch (RentalException ex) {
            assertThat(ex).isNotNull();
            assertThat(ex.isEmpty()).isFalse();
            assertThat(ex.getExceptions()).hasSize(4);
            assertThat(ex.getExceptions()).extracting(Exception::getMessage)
                    .containsExactly(
                            "Rental with id '1' is not rented by customer with id '" + customerId + "'",
                            "Rental with id '2' is not rented by customer with id '" + customerId + "'",
                            "Rental with id '3' is not rented by customer with id '" + customerId + "'",
                            "Rental with id '4' is not rented by customer with id '" + customerId + "'"
                    );
        }

        assertThat(result).isNull();
    }
}
