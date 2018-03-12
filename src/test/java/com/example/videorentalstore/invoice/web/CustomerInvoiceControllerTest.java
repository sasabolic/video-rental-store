package com.example.videorentalstore.invoice.web;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.invoice.InvoiceDataFixtures;
import com.example.videorentalstore.invoice.InvoiceService;
import com.example.videorentalstore.invoice.web.dto.assembler.DefaultInvoiceResponseAssembler;
import com.example.videorentalstore.invoice.web.dto.assembler.InvoiceResponseAssembler;
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
        public InvoiceResponseAssembler invoiceResponseAssembler() {
            return new DefaultInvoiceResponseAssembler();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @Test
    public void whenGetForCustomerAndTypeThenReturnStatusOK() throws Exception {
        final long customerId = 12L;
        final Customer customer = spy(CustomerDataFixtures.customer());

        given(customer.getId()).willReturn(customerId);
        given(this.invoiceService.calculate(anyLong(), isA(Invoice.Type.class))).willReturn(InvoiceDataFixtures.invoice(BigDecimal.TEN, customer));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/invoices/{type}", customerId, "up-front")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetForCustomerAndTypeThenReturnJson() throws Exception {
        final long customerId = 12L;
        final Customer customer = spy(CustomerDataFixtures.customer());

        given(customer.getId()).willReturn(customerId);

        given(this.invoiceService.calculate(anyLong(), isA(Invoice.Type.class))).willReturn(InvoiceDataFixtures.invoice(BigDecimal.TEN, customer));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/invoices/{type}", customerId, "up-front")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.amount", equalTo(10)));
    }

    @Test
    public void whenGetForNonExistingCustomerAndTypeThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.invoiceService.calculate(anyLong(), isA(Invoice.Type.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/invoices/{type}", customerId, "up-front")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetForNonExistingCustomerAndTypeThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.invoiceService.calculate(anyLong(), isA(Invoice.Type.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/invoices/{type}", customerId, "up-front")
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
    public void whenGetForCustomerAndNonExistingTypeThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/invoices/{type}", 1L, "non-existing")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetForCustomerAndNonExistingTypeThenReturnJsonError() throws Exception {
        final String type = "non-existing";
        final String message = "Invoice type of for path variable '" + type + "' does not exist.";

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/invoices/{type}", 1L, type)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}
