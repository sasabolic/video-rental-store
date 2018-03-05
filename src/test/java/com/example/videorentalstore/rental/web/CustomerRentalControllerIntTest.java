package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.AbstractWebIntTest;
import com.example.videorentalstore.film.FilmRepository;
import com.example.videorentalstore.rental.CreateRentalCommand;
import com.example.videorentalstore.rental.CreateRentalsCommand;
import com.example.videorentalstore.rental.RentalRepository;
import com.example.videorentalstore.rental.RentalService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerRentalControllerIntTest extends AbstractWebIntTest {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Before
    public void setUp() {
        rentalRepository.deleteAll();
        filmRepository.findAll().forEach(f -> {
            filmRepository.save(f.increaseBy(1));
        });
    }

    @Test
    public void whenGetRentalsForCustomerThenReturnListOfFilms() throws Exception {
        final Long customerId = 1L;
        final Long filmId = 1L;
        final int daysRented = 10;
        rentalService.create(new CreateRentalsCommand(customerId, Arrays.asList(new CreateRentalCommand(filmId, daysRented))));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{id}/rentals", customerId)
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].film.id", equalTo(filmId.intValue())))
                .andExpect(jsonPath("$[0].film.name", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[0].film.type", equalTo("NEW_RELEASE")))
                .andExpect(jsonPath("$[0].film.quantity", equalTo(3)))
                .andExpect(jsonPath("$[0].daysRented", equalTo(daysRented)));
    }
}
