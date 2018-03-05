package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.customer.web.CreateRentalRequest;
import com.example.videorentalstore.film.FilmDataFixtures;
import com.example.videorentalstore.film.FilmRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class RentalServiceTest {

    private RentalService rentalService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FilmRepository filmRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        rentalService = new DefaultRentalService(customerRepository, filmRepository);
    }

    @Test
    public void whenCreatingRentalsThenReturnCorrectResult() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        doReturn(Optional.of(FilmDataFixtures.newReleaseFilm("Matrix 11"))).when(filmRepository).findById(eq(1L));
        doReturn(Optional.of(FilmDataFixtures.regularReleaseFilm("Spider Man"))).when(filmRepository).findById(eq(2L));
        doReturn(Optional.of(FilmDataFixtures.regularReleaseFilm("Spider Man 2"))).when(filmRepository).findById(eq(3L));
        doReturn(Optional.of(FilmDataFixtures.oldReleaseFilm("Out of Africa"))).when(filmRepository).findById(eq(4L));

        final List<CreateRentalRequest> createRentalRequests = Arrays.asList(
                new CreateRentalRequest(1L, 1),
                new CreateRentalRequest(2L, 5),
                new CreateRentalRequest(3L, 2),
                new CreateRentalRequest(4L, 7));

        final RentalResponse rentalResponse = rentalService.create(1L, createRentalRequests);

        assertThat(rentalResponse).isNotNull();
        assertThat(rentalResponse).hasFieldOrPropertyWithValue("amount", BigDecimal.valueOf(250));
        assertThat(rentalResponse.getRentals()).hasSize(4);
        assertThat(rentalResponse.getRentals()).extracting(r -> r.getFilm().getName()).containsExactly("Matrix 11", "Spider Man", "Spider Man 2", "Out of Africa");
        assertThat(rentalResponse.getRentals()).extracting(r -> r.getDaysRented()).containsExactly(1, 5, 2, 7);
        assertThat(rentalResponse.getRentals()).extracting(r -> r.getStatus()).containsOnly(Rental.Status.RENTED);
    }

    @Test
    public void whenReturningBackRentalsThenReturnCorrectResult() {
        doReturn(Optional.of(CustomerDataFixtures.customerWithRentals(3))).when(customerRepository).findById(anyLong());

        final RentalResponse rentalResponse = rentalService.returnBack(1L, Arrays.asList(1L, 2L, 3L, 4L));

        assertThat(rentalResponse).isNotNull();
        assertThat(rentalResponse).hasFieldOrPropertyWithValue("amount", BigDecimal.valueOf(110));
        assertThat(rentalResponse.getRentals()).hasSize(4);
        assertThat(rentalResponse.getRentals()).extracting(r -> r.getFilm().getName()).containsExactly("Matrix 11", "Spider Man", "Spider Man 2", "Out of Africa");
        assertThat(rentalResponse.getRentals()).extracting(r -> r.getEndDate()).isNotNull();
        assertThat(rentalResponse.getRentals()).extracting(r -> r.getStatus()).containsOnly(Rental.Status.RETURNED);
    }

    @Test
    public void whenCreatingRentalsOfNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        final List<CreateRentalRequest> createRentalRequests = Arrays.asList(
                new CreateRentalRequest(1L, 1),
                new CreateRentalRequest(2L, 5),
                new CreateRentalRequest(3L, 2),
                new CreateRentalRequest(4L, 7));

        rentalService.create(customerId, createRentalRequests);
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
    public void whenCreatingRentalsOfNonExistingFilmsThenThrowException() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(anyLong());

        final List<CreateRentalRequest> createRentalRequests = Arrays.asList(
                new CreateRentalRequest(1L, 1),
                new CreateRentalRequest(2L, 5),
                new CreateRentalRequest(3L, 2),
                new CreateRentalRequest(4L, 7));

        RentalResponse rentalResponse = null;
        try {
            rentalResponse = rentalService.create(1L, createRentalRequests);
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

        assertThat(rentalResponse).isNull();
    }

    @Test
    public void whenReturningBackRentalsOfNonExistingFilmsThenThrowException() {
        final long customerId = 1L;
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        RentalResponse rentalResponse = null;
        try {
            rentalResponse = rentalService.returnBack(customerId, Arrays.asList(1L, 2L, 3L, 4L));
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

        assertThat(rentalResponse).isNull();
    }

    @Test
    public void whenReturningBackAlreadyReturnedRentalsThenThrowException() {
        final long customerId = 1L;
        doReturn(Optional.of(CustomerDataFixtures.customerWithReturnedRentals())).when(customerRepository).findById(anyLong());

        RentalResponse rentalResponse = null;
        try {
            rentalResponse = rentalService.returnBack(customerId, Arrays.asList(1L, 2L, 3L, 4L));
        } catch (RentalException ex) {
            assertThat(ex).isNotNull();
            assertThat(ex.isEmpty()).isFalse();
            assertThat(ex.getExceptions()).hasSize(4);
            assertThat(ex.getExceptions()).extracting(Exception::getMessage)
                    .containsExactly(
                            "Cannot mark rental with id '1' as RETURNED that is currently not RENTED! Current status: RETURNED.",
                            "Cannot mark rental with id '2' as RETURNED that is currently not RENTED! Current status: RETURNED.",
                            "Cannot mark rental with id '3' as RETURNED that is currently not RENTED! Current status: RETURNED.",
                            "Cannot mark rental with id '4' as RETURNED that is currently not RENTED! Current status: RETURNED."
                    );
        }

        assertThat(rentalResponse).isNull();
    }
}
