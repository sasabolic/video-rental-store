package com.example.videorentalstore.billing;

import com.example.videorentalstore.inventory.Film;
import com.example.videorentalstore.rental.Rental;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class BillingServiceTest {

    private BillingService billingService;

    @Before
    public void setUp() {
        billingService = Mockito.spy(new BillingService());
    }

    @Test
    public void test() {
        Rental rental = mock(Rental.class);
        Rental rental1 = mock(Rental.class);
        Rental rental2 = mock(Rental.class);
        Rental rental3 = mock(Rental.class);

        Film film = mock(Film.class);
        Film film1 = mock(Film.class);
        Film film2 = mock(Film.class);
        Film film3 = mock(Film.class);



        doReturn(film).when(rental).getFilm();
        doReturn(1).when(rental).getDays();
        doReturn(Instant.now()).when(film).getReleaseDate();
        doReturn("Matrix 11").when(film).getName();

        LocalDateTime regularDate = LocalDateTime.of(2016, Month.AUGUST, 18, 6, 17, 10);

        doReturn(film1).when(rental1).getFilm();
        doReturn(5).when(rental1).getDays();
        doReturn(regularDate.toInstant(ZoneOffset.UTC)).when(film1).getReleaseDate();
        doReturn("Spider Man").when(film1).getName();

        doReturn(film2).when(rental2).getFilm();
        doReturn(2).when(rental2).getDays();
        doReturn(regularDate.toInstant(ZoneOffset.UTC)).when(film2).getReleaseDate();
        doReturn("Spider Man 2").when(film2).getName();

        LocalDateTime oldDate = LocalDateTime.of(2014, Month.AUGUST, 18, 6, 17, 10);
        doReturn(film3).when(rental3).getFilm();
        doReturn(7).when(rental3).getDays();
        doReturn(oldDate.toInstant(ZoneOffset.UTC)).when(film3).getReleaseDate();
        doReturn("Out of Africa").when(film3).getName();

        billingService.calculatePrice(Arrays.asList(rental, rental1, rental2, rental3));
    }
}
