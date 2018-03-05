package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.RentalDataFixtures;
import com.example.videorentalstore.rental.RentalResponse;
import com.example.videorentalstore.rental.RentalService;
import com.example.videorentalstore.rental.web.CustomerRentalController;
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
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
    public void whenCreateRentalsForCustomerThenReturnCorrectResponse() throws Exception {

        doReturn(new RentalResponse(BigDecimal.valueOf(250), RentalDataFixtures.rentals())).when(rentalService).create(isA(Long.class), isA(List.class));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{id}/rentals", 12)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.amount", equalTo(250)));

    }

    @Test
    public void whenGetRentalsForCustomerThenReturnListOfFilms() throws Exception {
        doReturn(RentalDataFixtures.rentals()).when(rentalService).findAll(isA(Long.class));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{id}/rentals", 12)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].film.name", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[1].film.name", equalTo("Spider Man")))
                .andExpect(jsonPath("$[2].film.name", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[3].film.name", equalTo("Out of Africa")));

    }
}