package io.sixhours.videorentalstore.rental.web;

import io.sixhours.videorentalstore.core.JsonConfig;
import io.sixhours.videorentalstore.customer.CustomerNotFoundException;
import io.sixhours.videorentalstore.film.FilmNotFoundException;
import io.sixhours.videorentalstore.film.Price;
import io.sixhours.videorentalstore.rental.*;
import io.sixhours.videorentalstore.rental.web.dto.assembler.BatchRentalResponseAssembler;
import io.sixhours.videorentalstore.rental.web.dto.assembler.DefaultBatchRentalResponseAssembler;
import io.sixhours.videorentalstore.rental.web.dto.assembler.DefaultRentalResponseAssembler;
import io.sixhours.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CustomerRentalController.class)
public class CustomerRentalControllerTest {

    @TestConfiguration
    @Import(JsonConfig.class)
    static class TestConfig {

        @Bean
        public RentalResponseAssembler rentalResponseAssembler() {
            return new DefaultRentalResponseAssembler();
        }

        @Bean
        public BatchRentalResponseAssembler batchRentalResponseAssembler() {
            return new DefaultBatchRentalResponseAssembler(rentalResponseAssembler());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @Test
    public void whenGetAllForCustomerThenReturnStatusOK() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong())).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", 12)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllForCustomerThenReturnJson() throws Exception {
        given(this.rentalService.findAllForCustomer(anyLong())).willReturn(RentalDataFixtures.rentals());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", 12)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
    public void whenGetAllForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.rentalService.findAllForCustomer(anyLong())).willThrow(new CustomerNotFoundException(message));

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

        given(this.rentalService.findAllForCustomer(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/rentals", customerId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenCreateForCustomerThenReturnStatusOK() throws Exception {
        given(rentalService.create(anyLong(), anyList())).willReturn(new BatchRental(Money.of(250, Price.CURRENCY_CODE), RentalDataFixtures.rentals()));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", 12)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateForCustomerThenReturnJson() throws Exception {
        final long customerId = 12L;
        given(rentalService.create(anyLong(), anyList())).willReturn(new BatchRental(Money.of(250, Price.CURRENCY_CODE), RentalDataFixtures.rentals()));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.amount", equalTo("RSD 250.00")))
                .andExpect(jsonPath("$.rentals").isArray())
                .andExpect(jsonPath("$.rentals", hasSize(4)))
                .andExpect(jsonPath("$.rentals[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$.rentals[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$.rentals[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$.rentals[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$.rentals[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$.rentals[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$.rentals[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$.rentals[3].days_rented", equalTo(7)));
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo(message)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    public void whenCreateForCustomerWithExistingRentalsThenReturnBadRequest() throws Exception {
        final Long customerId = 1L;
        final String message = "Could not create new rentals. Customer with '" + customerId + "' contains active rentals.";

        given(rentalService.create(anyLong(), anyList())).willThrow(new RentalException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateForCustomerExistingRentalsThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Could not create new rentals. Customer with '" + customerId + "' contains active rentals.";

        given(rentalService.create(anyLong(), anyList())).willThrow(new RentalException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/rentals", customerId)
                .content(RentalDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo(message)));
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    public void whenReturnBackThenReturnStatusOK() throws Exception {
        given(rentalService.returnBack(anyLong(), anyList())).willReturn(new BatchRental(Money.of(110, Price.CURRENCY_CODE), RentalDataFixtures.rentals()));

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
        final List<Rental> rentals = RentalDataFixtures.returnedRentals();

        given(rentalService.returnBack(anyLong(), anyList())).willReturn(new BatchRental(Money.of(110, Price.CURRENCY_CODE), rentals));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content(RentalDataFixtures.returnJson())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.amount", equalTo("RSD 110.00")))
                .andExpect(jsonPath("$.rentals").isArray())
                .andExpect(jsonPath("$.rentals", hasSize(4)))
                .andExpect(jsonPath("$.rentals[0].days_rented", equalTo(1)))
                .andExpect(jsonPath("$.rentals[0].film_title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$.rentals[0].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[0].end_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[1].days_rented", equalTo(5)))
                .andExpect(jsonPath("$.rentals[1].film_title", equalTo("Spider Man")))
                .andExpect(jsonPath("$.rentals[1].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[1].end_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[2].days_rented", equalTo(2)))
                .andExpect(jsonPath("$.rentals[2].film_title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$.rentals[2].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[2].end_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[3].days_rented", equalTo(7)))
                .andExpect(jsonPath("$.rentals[3].film_title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$.rentals[3].start_date", is(notNullValue())))
                .andExpect(jsonPath("$.rentals[3].end_date", is(notNullValue())));
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .content("[\n" +
                        "  {\"id\": 1},\n" +
                        "  {\"id\": 2},\n" +
                        "  {\"id\": " + rentalId1 + "},\n" +
                        "  {\"id\": " + rentalId2 + "}\n" +
                        "]")
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
                .content("[\n" +
                        "  {\"id\": 1},\n" +
                        "  {\"id\": 2},\n" +
                        "  {\"id\": " + rentalId1 + "},\n" +
                        "  {\"id\": " + rentalId2 + "}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .content("[]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .content("[\n" +
                        "  {\"id\": null}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenReturnBackForCustomerWithInvalidFieldsThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/customers/{customerId}/rentals", 12)
                .content("[\n" +
                        "  {\"id\": null}\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }
}
