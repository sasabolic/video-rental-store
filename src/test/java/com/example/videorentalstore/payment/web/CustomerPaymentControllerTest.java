package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.invoice.Invoice;
import com.example.videorentalstore.payment.PaymentDataFixtures;
import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.Receipt;
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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CustomerPaymentController.class)
public class CustomerPaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void whenCreateForCustomerThenReturnStatusCreated() throws Exception {
        final long customerId = 12L;
        final long receiptId = 1L;
        final BigDecimal amount = BigDecimal.valueOf(250);
        final Receipt receipt = spy(PaymentDataFixtures.receipt(amount));

        given(receipt.getId()).willReturn(receiptId);
        given(paymentService.pay(anyLong(), isA(Invoice.Type.class), isA(BigDecimal.class))).willReturn(receipt);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/payments", customerId)
                .content(PaymentDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForCustomerThenReturnLocationHeader() throws Exception {
        final long customerId = 12L;
        final long receiptId = 1L;
        final BigDecimal amount = BigDecimal.valueOf(250);
        final Receipt receipt = spy(PaymentDataFixtures.receipt(amount));

        given(receipt.getId()).willReturn(receiptId);
        given(paymentService.pay(anyLong(), isA(Invoice.Type.class), isA(BigDecimal.class))).willReturn(receipt);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/payments", customerId)
                .content(PaymentDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/receipts/" + receiptId));
    }

    @Test
    public void whenCreateForNonExistingCustomerThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(paymentService.pay(anyLong(), isA(Invoice.Type.class), isA(BigDecimal.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/payments", customerId)
                .content(PaymentDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForNonExistingCustomerThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(paymentService.pay(anyLong(), isA(Invoice.Type.class), isA(BigDecimal.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/{customerId}/payments", customerId)
                .content(PaymentDataFixtures.json())
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
    public void whenGetAllForCustomerThenReturnStatusMethodNotAllowed() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/payments", 12L)
                .content(PaymentDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void whenGetAllForCustomerThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}/payments", 12L)
                .content(PaymentDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(405)))
                .andExpect(jsonPath("$.message", equalTo("HTTP method GET is not supported by this URL")));
    }
}
