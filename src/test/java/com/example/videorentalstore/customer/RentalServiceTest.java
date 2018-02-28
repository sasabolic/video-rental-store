package com.example.videorentalstore.customer;

import com.example.videorentalstore.customer.web.RentalItem;
import com.example.videorentalstore.inventory.FilmRepository;
import com.example.videorentalstore.inventory.web.FilmDataFixtures;
import com.example.videorentalstore.rental.DefaultRentalService;
import com.example.videorentalstore.rental.RentalService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
        doReturn(CustomerDataFixtures.customer()).when(customerRepository).findOne(Matchers.eq(1L));

        doReturn(FilmDataFixtures.newReleaseFilm("Matrix 11")).when(filmRepository).findOne(Matchers.eq(1L));
        doReturn(FilmDataFixtures.regularReleaseFilm("Spider Man")).when(filmRepository).findOne(Matchers.eq(2L));
        doReturn(FilmDataFixtures.regularReleaseFilm("Spider Man 2")).when(filmRepository).findOne(Matchers.eq(3L));
        doReturn(FilmDataFixtures.oldReleaseFilm("Out of Africa")).when(filmRepository).findOne(Matchers.eq(4L));

        final List<RentalItem> rentalItems = Arrays.asList(
                new RentalItem(1L, 1),
                new RentalItem(2L, 5),
                new RentalItem(3L, 2),
                new RentalItem(4L, 7));

        final BigDecimal rentals = rentalService.create(1L, rentalItems);
    }
}
