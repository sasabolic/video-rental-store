package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CustomerRentalController.class)
public class CustomerRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @Test
    public void whenCreateRentalsThenReturnCorrectResponse() throws Exception {
        doReturn(new Receipt(BigDecimal.valueOf(250), RentalDataFixtures.rentals())).when(rentalService).create(isA(CreateRentalsCommand.class));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{id}/rentals", 12)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.amount", equalTo(250)))
                .andExpect(jsonPath("$.rentals").isArray())
                .andExpect(jsonPath("$.rentals", hasSize(4)))
                .andExpect(jsonPath("$.rentals[0].status", equalTo("RENTED")))
                .andExpect(jsonPath("$.rentals[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$.rentals[0].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[0].end_date", is(nullValue())))
                .andExpect(jsonPath("$.rentals[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$.rentals[1].status", equalTo("RENTED")))
                .andExpect(jsonPath("$.rentals[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$.rentals[1].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$.rentals[2].status", equalTo("RENTED")))
                .andExpect(jsonPath("$.rentals[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$.rentals[2].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$.rentals[3].status", equalTo("RENTED")))
                .andExpect(jsonPath("$.rentals[3].days_rented", equalTo(7)))
                .andExpect(jsonPath("$.rentals[3].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[3].film_title", equalTo("Out of Africa")));

    }

    @Test
    public void whenReturnBackRentalsThenReturnCorrectResponse() throws Exception {
        doReturn(new Receipt(BigDecimal.valueOf(110), RentalDataFixtures.returnedRentals())).when(rentalService).returnBack(isA(ReturnRentalsCommand.class));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{id}/rentals", 12)
                .content(RentalDataFixtures.idsJson())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.amount", equalTo(110)))
                .andExpect(jsonPath("$.rentals").isArray())
                .andExpect(jsonPath("$.rentals", hasSize(4)))
                .andExpect(jsonPath("$.rentals[0].status", equalTo("RETURNED")))
                .andExpect(jsonPath("$.rentals[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$.rentals[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$.rentals[0].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[0].end_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[1].status", equalTo("RETURNED")))
                .andExpect(jsonPath("$.rentals[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$.rentals[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$.rentals[1].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[1].end_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[2].status", equalTo("RETURNED")))
                .andExpect(jsonPath("$.rentals[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$.rentals[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$.rentals[2].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[2].end_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[3].status", equalTo("RETURNED")))
                .andExpect(jsonPath("$.rentals[3].days_rented", equalTo(7)))
                .andExpect(jsonPath("$.rentals[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$.rentals[3].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[3].end_date", is(notNullValue())));

    }

    @Test
    public void whenGetRentalsForCustomerThenReturnListOfFilms() throws Exception {
        doReturn(RentalDataFixtures.rentals()).when(rentalService).findAllRentedForCustomer(isA(Long.class));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{id}/rentals", 12)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[0].status", equalTo("RENTED")))
                .andExpect(jsonPath("$[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[1].status", equalTo("RENTED")))
                .andExpect(jsonPath("$[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[2].status", equalTo("RENTED")))
                .andExpect(jsonPath("$[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$[3].status", equalTo("RENTED")))
                .andExpect(jsonPath("$[3].days_rented", equalTo(7)));

    }
}
