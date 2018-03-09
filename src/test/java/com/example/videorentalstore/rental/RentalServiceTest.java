package com.example.videorentalstore.rental;

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

    @Mock
    private RentalRepository rentalRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        rentalService = new DefaultRentalService(customerRepository, filmRepository, rentalRepository);
    }

    @Test
    public void whenCreatingRentalsThenReturnCorrectResult() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        doReturn(Optional.of(FilmDataFixtures.newReleaseFilm("Matrix 11"))).when(filmRepository).findById(eq(1L));
        doReturn(Optional.of(FilmDataFixtures.regularReleaseFilm("Spider Man"))).when(filmRepository).findById(eq(2L));
        doReturn(Optional.of(FilmDataFixtures.regularReleaseFilm("Spider Man 2"))).when(filmRepository).findById(eq(3L));
        doReturn(Optional.of(FilmDataFixtures.oldReleaseFilm("Out of Africa"))).when(filmRepository).findById(eq(4L));

        final List<CreateRentalCommand> createRentalCommands = Arrays.asList(
                new CreateRentalCommand(1L, 1),
                new CreateRentalCommand(2L, 5),
                new CreateRentalCommand(3L, 2),
                new CreateRentalCommand(4L, 7));

        final Receipt receipt = rentalService.create(new CreateRentalsCommand(1L, createRentalCommands));

        assertThat(receipt).isNotNull();
        assertThat(receipt).hasFieldOrPropertyWithValue("amount", BigDecimal.valueOf(250));
        assertThat(receipt.getRentals()).hasSize(4);
        assertThat(receipt.getRentals()).extracting(r -> r.getFilm().getTitle()).containsExactly("Matrix 11", "Spider Man", "Spider Man 2", "Out of Africa");
        assertThat(receipt.getRentals()).extracting(r -> r.getDaysRented()).containsExactly(1, 5, 2, 7);
        assertThat(receipt.getRentals()).extracting(r -> r.isActive()).containsOnly(true);
    }

    @Test
    public void whenReturningBackRentalsThenReturnCorrectResult() {
        doReturn(Optional.of(CustomerDataFixtures.customerWithRentals(3))).when(customerRepository).findById(anyLong());

        final Receipt receipt = rentalService.returnBack(new ReturnRentalsCommand(1L, Arrays.asList(new ReturnRentalCommand(1L), new ReturnRentalCommand(2L), new ReturnRentalCommand(3L), new ReturnRentalCommand(4L))));

        assertThat(receipt).isNotNull();
        assertThat(receipt).hasFieldOrPropertyWithValue("amount", BigDecimal.valueOf(110));
        assertThat(receipt.getRentals()).hasSize(4);
        assertThat(receipt.getRentals()).extracting(r -> r.getFilm().getTitle()).containsExactly("Matrix 11", "Spider Man", "Spider Man 2", "Out of Africa");
        assertThat(receipt.getRentals()).extracting(r -> r.getEndDate()).isNotNull();
        assertThat(receipt.getRentals()).extracting(r -> r.isActive()).containsOnly(false);
    }

    @Test
    public void whenCreatingRentalsOfNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        final List<CreateRentalCommand> createRentalCommands = Arrays.asList(
                new CreateRentalCommand(1L, 1),
                new CreateRentalCommand(2L, 5),
                new CreateRentalCommand(3L, 2),
                new CreateRentalCommand(4L, 7));

        rentalService.create(new CreateRentalsCommand(customerId, createRentalCommands));
    }

    @Test
    public void whenReturningBackRentalsOfNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(anyLong());

        rentalService.returnBack(new ReturnRentalsCommand(customerId, Arrays.asList(new ReturnRentalCommand(1L), new ReturnRentalCommand(2L), new ReturnRentalCommand(3L), new ReturnRentalCommand(4L))));
    }

    @Test
    public void whenCreatingRentalsOfNonExistingFilmsThenThrowException() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        doReturn(Optional.ofNullable(null)).when(filmRepository).findById(anyLong());

        final List<CreateRentalCommand> createRentalCommands = Arrays.asList(
                new CreateRentalCommand(1L, 1),
                new CreateRentalCommand(2L, 5),
                new CreateRentalCommand(3L, 2),
                new CreateRentalCommand(4L, 7));

        Receipt receipt = null;
        try {
            receipt = rentalService.create(new CreateRentalsCommand(1L, createRentalCommands));
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

        assertThat(receipt).isNull();
    }

    @Test
    public void whenReturningBackRentalsOfNonExistingFilmsThenThrowException() {
        final long customerId = 1L;
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(anyLong());

        Receipt receipt = null;
        try {
            receipt = rentalService.returnBack(new ReturnRentalsCommand(customerId, Arrays.asList(new ReturnRentalCommand(1L), new ReturnRentalCommand(2L), new ReturnRentalCommand(3L), new ReturnRentalCommand(4L))));
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

        assertThat(receipt).isNull();
    }

//    @Test
//    public void whenReturningBackAlreadyReturnedRentalsThenThrowException() {
//        final long customerId = 1L;
//        doReturn(Optional.of(CustomerDataFixtures.customerWithReturnedRentals())).when(customerRepository).findById(anyLong());
//
//        Receipt receipt = null;
//        try {
//            receipt = rentalService.returnBack(new ReturnRentalsCommand(customerId, Arrays.asList(new ReturnRentalCommand(1L), new ReturnRentalCommand(2L), new ReturnRentalCommand(3L), new ReturnRentalCommand(4L))));
//        } catch (RentalException ex) {
//            assertThat(ex).isNotNull();
//            assertThat(ex.isEmpty()).isFalse();
//            assertThat(ex.getExceptions()).hasSize(4);
//            assertThat(ex.getExceptions()).extracting(Exception::getMessage)
//                    .containsExactly(
//                            "Cannot mark rental with id '1' as RETURNED that is currently not ACTIVE! Current status: RETURNED.",
//                            "Cannot mark rental with id '2' as RETURNED that is currently not ACTIVE! Current status: RETURNED.",
//                            "Cannot mark rental with id '3' as RETURNED that is currently not ACTIVE! Current status: RETURNED.",
//                            "Cannot mark rental with id '4' as RETURNED that is currently not ACTIVE! Current status: RETURNED."
//                    );
//        }
//
//        assertThat(receipt).isNull();
//    }
}
