package com.example.videorentalstore.invoice.web;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.InvoiceDataFixtures;
import com.example.videorentalstore.invoice.InvoiceService;
import com.example.videorentalstore.invoice.InvoiceType;
import com.example.videorentalstore.invoice.web.dto.assembler.DefaultInvoiceResponseAssembler;
import com.example.videorentalstore.invoice.web.dto.assembler.InvoiceResponseAssembler;
import com.example.videorentalstore.rental.RentalDataFixtures;
import com.example.videorentalstore.rental.web.dto.assembler.DefaultRentalResponseAssembler;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CustomerInvoiceController.class)
public class CustomerInvoiceControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public RentalResponseAssembler rentalResponseAssembler() {
            return new DefaultRentalResponseAssembler();
        }

        @Bean
        public InvoiceResponseAssembler invoiceResponseAssembler() {
            return new DefaultInvoiceResponseAssembler(rentalResponseAssembler());
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @Test
    public void whenCreateForCustomerThenReturnStatusCreated() throws Exception {
        final long customerId = 12L;
        final long invoiceId = 1;
        Customer customer = CustomerDataFixtures.customerWithRentals(0);
        final Invoice invoice = spy(InvoiceDataFixtures.invoice(customer));

        given(invoice.getId()).willReturn(invoiceId);
        given(this.invoiceService.create(anyLong(), isA(InvoiceType.class))).willReturn(invoice);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/invoices", customerId)
                .content(InvoiceDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForCustomerThenReturnLocationHeader() throws Exception {
        final long customerId = 12L;
        final long invoiceId = 1;
        Customer customer = CustomerDataFixtures.customerWithRentals(0);
        final Invoice invoice = spy(InvoiceDataFixtures.invoice(customer));

        given(invoice.getId()).willReturn(invoiceId);
        given(this.invoiceService.create(anyLong(), isA(InvoiceType.class))).willReturn(invoice);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/invoices", customerId)
                .content(InvoiceDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/invoices/" + invoiceId));
    }

    @Test
    public void whenCreateForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(invoiceService.create(anyLong(), isA(InvoiceType.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/invoices", customerId)
                .content(InvoiceDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForNonExistingCustomerThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(invoiceService.create(anyLong(), isA(InvoiceType.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/invoices", customerId)
                .content(InvoiceDataFixtures.json())
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
    public void whenCreateForCustomerEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/invoices", 12)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateForCustomerEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/invoices", 12)
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
}
