package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.payment.PaymentDataFixtures;
import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.web.dto.assembler.DefaultReceiptResponseAssembler;
import com.example.videorentalstore.payment.web.dto.assembler.ReceiptResponseAssembler;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CustomerReceiptController.class)
public class CustomerReceiptControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReceiptResponseAssembler receiptResponseAssembler() {
            return new DefaultReceiptResponseAssembler();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;


    @Test
    public void whenGetAllForCustomerWithStatusThenReturnStatusOK() throws Exception {
        final long customerId = 12L;
        final long receiptId = 1L;
        final long receiptId1 = 1L;
        final BigDecimal amount = BigDecimal.valueOf(250);
        final Receipt receipt = spy(PaymentDataFixtures.receipt(amount));
        final Receipt receipt1 = spy(PaymentDataFixtures.receipt(amount));

        given(receipt.getId()).willReturn(receiptId);
        given(receipt1.getId()).willReturn(receiptId1);

        given(this.paymentService.findAllForCustomer(anyLong())).willReturn(Arrays.asList(receipt, receipt1));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/receipts", customerId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllForCustomerThenReturnJson() throws Exception {
        final long customerId = 12L;
        final long receiptId = 1L;
        final long receiptId1 = 2L;
        final BigDecimal amount = BigDecimal.valueOf(250);
        final BigDecimal amount1 = BigDecimal.valueOf(110);
        final Receipt receipt = spy(PaymentDataFixtures.receipt(amount));
        final Receipt receipt1 = spy(PaymentDataFixtures.receipt(amount1));

        given(receipt.getId()).willReturn(receiptId);
        given(receipt1.getId()).willReturn(receiptId1);

        given(this.paymentService.findAllForCustomer(anyLong())).willReturn(Arrays.asList(receipt, receipt1));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/receipts", customerId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.receipts").isArray())
                .andExpect(jsonPath("$._embedded.receipts", hasSize(2)))
                .andExpect(jsonPath("$._embedded.receipts[0].amount", equalTo(250)))
                .andExpect(jsonPath("$._embedded.receipts[0]._links.self.href", equalTo("http://localhost/receipts/1")))
                .andExpect(jsonPath("$._embedded.receipts[1].amount", equalTo(110)))
                .andExpect(jsonPath("$._embedded.receipts[1]._links.self.href", equalTo("http://localhost/receipts/2")))
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/customers/12/receipts")))
                .andExpect(jsonPath("$._links.customer.href", equalTo("http://localhost/customers/" + customerId)));
    }

    @Test
    public void whenGetAllForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.paymentService.findAllForCustomer(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/receipts", customerId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetAllForNonExistingCustomerThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.paymentService.findAllForCustomer(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/receipts", customerId)
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
}
