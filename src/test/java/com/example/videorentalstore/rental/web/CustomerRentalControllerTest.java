package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.film.FilmNotFoundException;
import com.example.videorentalstore.rental.*;
import com.example.videorentalstore.rental.web.dto.assembler.DefaultReceiptResponseAssembler;
import com.example.videorentalstore.rental.web.dto.assembler.DefaultRentalResponseAssembler;
import com.example.videorentalstore.rental.web.dto.assembler.ReceiptResponseAssembler;
import com.example.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CustomerRentalController.class)
public class CustomerRentalControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RentalResponseAssembler rentalConverter() {
            return new DefaultRentalResponseAssembler();
        }

        @Bean
        public ReceiptResponseAssembler receiptConverter() {
            return new DefaultReceiptResponseAssembler(rentalConverter());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @Test
    public void whenGetAllForCustomerThenReturnStatusOK() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong(), nullable(Rental.Status.class))).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", 12)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllForCustomerThenReturnJson() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong(), nullable(Rental.Status.class))).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", 12)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$[3].days_rented", equalTo(7)));
    }

    @Test
    public void whenGetAllForCustomerWithStatusThenReturnStatusOK() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong(), isA(Rental.Status.class))).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals?status={status}", 12, Rental.Status.UP_FRONT_PAYMENT_EXPECTED)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllForCustomerWithStatusThenReturnJson() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong(), isA(Rental.Status.class))).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals?status={status}", 12, Rental.Status.UP_FRONT_PAYMENT_EXPECTED)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$[3].days_rented", equalTo(7)));
    }

    @Test
    public void whenGetAllForCustomerWithNonExistingStatusThenReturnStatusBadRequest() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong(), nullable(Rental.Status.class))).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals?status={status}", 12, "NON_EXISTING")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetAllForCustomerWithNonExistingStatusThenReturnJsonError() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong(), nullable(Rental.Status.class))).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals?status={status}", 12, "NON_EXISTING")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)));
    }

    @Test
    public void whenGetAllForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.rentalService.findAllForCustomer(anyLong(), nullable(Rental.Status.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", customerId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetAllForNonExistingCustomerThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.rentalService.findAllForCustomer(anyLong(), nullable(Rental.Status.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", customerId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenCreateForCustomerThenReturnStatusCreated() throws Exception {
        given(rentalService.create(isA(BatchRentalCreateCommand.class))).willReturn(new Receipt(BigDecimal.valueOf(250), RentalDataFixtures.rentals()));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForCustomerThenReturnLocationHeader() throws Exception {
        final int customerId = 12;

        given(rentalService.create(isA(BatchRentalCreateCommand.class))).willReturn(new Receipt(BigDecimal.valueOf(250), RentalDataFixtures.rentals()));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("http://localhost/customers/" + customerId + "/rentals?status=UP_FRONT_PAYMENT_EXPECTED")));
    }

    @Test
    public void whenCreateForCustomerThenReturnJson() throws Exception {
        given(rentalService.create(anyLong(), anyList())).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$[0].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[0].return_date", is(nullValue())))
                .andExpect(jsonPath("$[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$[1].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$[2].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[3].days_rented", equalTo(7)))
                .andExpect(jsonPath("$[3].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[3].film_title", equalTo("Out of Africa")));
    }

    @Test
    public void whenCreateForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(rentalService.create(anyLong(), anyList())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForNonExistingCustomerThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(rentalService.create(anyLong(), anyList())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenCreateForCustomerNonExistingFilmsThenReturnBadRequest() throws Exception {
        final Long filmId1 = 10L;
        final Long filmId2 = 23L;
        final String message = "Could not create rentals";
        final String message1 = "Film with id '" + filmId1 + "' does not exist";
        final String message2 = "Film with id '" + filmId2 + "' does not exist";

        given(rentalService.create(anyLong(), anyList())).willThrow(new RentalException(message, Arrays.asList(new FilmNotFoundException(message1), new FilmNotFoundException(message2))));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 1L)
                .content("[\n" +
                        "  {\"film_id\": 1, \"days_rented\": 10},\n" +
                        "  {\"film_id\": " + filmId1 + ", \"days_rented\": 10},\n" +
                        "  {\"film_id\": " + filmId2 + ", \"days_rented\": 10}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateForCustomerNonExistingFilmsThenReturnJsonError() throws Exception {
        final Long filmId1 = 10L;
        final Long filmId2 = 23L;
        final String message = "Could not create rentals";
        final String message1 = "Film with id '" + filmId1 + "' does not exist";
        final String message2 = "Film with id '" + filmId2 + "' does not exist";


        given(rentalService.create(anyLong(), anyList())).willThrow(new RentalException(message, Arrays.asList(new FilmNotFoundException(message1), new FilmNotFoundException(message2))));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 1L)
                .content("[\n" +
                        "  {\"film_id\": 1, \"days_rented\": 10},\n" +
                        "  {\"film_id\": " + filmId1 + ", \"days_rented\": 10},\n" +
                        "  {\"film_id\": " + filmId2 + ", \"days_rented\": 10}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo(message)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    public void whenCreateForCustomerEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content("[]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateForCustomerEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content("[]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void whenCreateForCustomerWithInvalidFieldsThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content("[\n" +
                        "  {\"film_id\": null, \"days_rented\": 0}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateForCustomerWithInvalidFieldsThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content("[\n" +
                        "  {\"film_id\": null, \"days_rented\": 0}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    public void whenReturnBackThenReturnStatusOK() throws Exception {
        given(rentalService.returnBack(anyLong(), anyList())).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content(RentalDataFixtures.returnJson())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenReturnBackThenReturnJson() throws Exception {
        final List<Rental> rentals = RentalDataFixtures.rentals();
        rentals.forEach(r -> r.markPaidUpFront().markActive().markReturned());

        given(rentalService.returnBack(anyLong(), anyList())).willReturn(rentals);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content(RentalDataFixtures.returnJson())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[0].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[0].return_date", is(notNullValue())))
                .andExpect(jsonPath("$[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[1].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[1].return_date", is(notNullValue())))
                .andExpect(jsonPath("$[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[2].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[2].return_date", is(notNullValue())))
                .andExpect(jsonPath("$[3].days_rented", equalTo(7)))
                .andExpect(jsonPath("$[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$[3].start_date", is(notNullValue())))
                .andExpect(jsonPath("$[3].return_date", is(notNullValue())));
    }

    @Test
    public void whenReturnBackForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(rentalService.returnBack(anyLong(), anyList())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.returnJson())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenReturnBackForNonExistingCustomerThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(rentalService.returnBack(anyLong(), anyList())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.returnJson())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenReturnBackForCustomerNonExistingRentalsThenReturnStatusBadRequest() throws Exception {
        final Long rentalId1 = 10L;
        final Long rentalId2 = 23L;
        final String message = "Could not create rentals";
        final String message1 = "Rental with id '" + rentalId1 + "' does not exist";
        final String message2 = "Rental with id '" + rentalId2 + "' does not exist";


        given(rentalService.returnBack(anyLong(), anyList())).willThrow(new RentalException(message, Arrays.asList(new RentalNotFoundException(message1), new RentalNotFoundException(message2))));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 1L)
                .content("{\n" +
                        "  \"rentals\": [\n" +
                        "    {\"rental_id\": 1},\n" +
                        "    {\"rental_id\": 2},\n" +
                        "    {\"rental_id\": " + rentalId1 + "},\n" +
                        "    {\"rental_id\": " + rentalId2 + "}\n" +
                        "  ]\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenReturnBackForCustomerNonExistingRentalsThenReturnJsonError() throws Exception {
        final Long rentalId1 = 10L;
        final Long rentalId2 = 23L;
        final String message = "Could not create rentals";
        final String message1 = "Rental with id '" + rentalId1 + "' does not exist";
        final String message2 = "Rental with id '" + rentalId2 + "' does not exist";


        given(rentalService.returnBack(anyLong(), anyList())).willThrow(new RentalException(message, Arrays.asList(new RentalNotFoundException(message1), new RentalNotFoundException(message2))));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 1L)
                .content("{\n" +
                        "  \"rentals\": [\n" +
                        "    {\"rental_id\": 1},\n" +
                        "    {\"rental_id\": 2},\n" +
                        "    {\"rental_id\": " + rentalId1 + "},\n" +
                        "    {\"rental_id\": " + rentalId2 + "}\n" +
                        "  ]\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo(message)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    public void whenReturnBackForCustomerEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content("[]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenReturnBackForCustomerEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void whenReturnBackForCustomerWithInvalidFieldsThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content("{\n" +
                        "  \"rentals\": [\n" +
                        "    {\"rental_id\": null}\n" +
                        "  ]\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenReturnBackForCustomerWithInvalidFieldsThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content("{\n" +
                        "  \"rentals\": [\n" +
                        "    {\"rental_id\": null}\n" +
                        "  ]\n" +
                        "}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}
