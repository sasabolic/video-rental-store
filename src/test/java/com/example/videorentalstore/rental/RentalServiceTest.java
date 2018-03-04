package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.CustomerDataFixtures;
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
    public void init() {
        rentalService = new DefaultRentalService(customerRepository, filmRepository);
    }


    @Test
    public void test() {
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

        rentalService.create(1L, createRentalRequests);
    }
}
