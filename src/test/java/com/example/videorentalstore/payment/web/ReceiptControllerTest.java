package com.example.videorentalstore.payment.web;

import com.example.videorentalstore.payment.PaymentDataFixtures;
import com.example.videorentalstore.payment.PaymentService;
import com.example.videorentalstore.payment.Receipt;
import com.example.videorentalstore.payment.ReceiptNotFoundException;
import com.example.videorentalstore.payment.web.dto.assembler.DefaultReceiptResponseAssembler;
import com.example.videorentalstore.payment.web.dto.assembler.ReceiptResponseAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = ReceiptController.class)
public class ReceiptControllerTest {

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
    public void whenGetThenReturnStatusOK() throws Exception {
        final long receiptId = 1L;
        final BigDecimal amount = BigDecimal.valueOf(250);
        final Receipt receipt = spy(PaymentDataFixtures.receipt(amount));
        given(receipt.getId()).willReturn(receiptId);

        given(this.paymentService.findById(anyLong())).willReturn(receipt);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/receipts/{receiptId}", receiptId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetThenReturnJson() throws Exception {
        final long receiptId = 1L;
        final BigDecimal amount = BigDecimal.valueOf(250);
        final Receipt receipt = spy(PaymentDataFixtures.receipt(amount));
        given(receipt.getId()).willReturn(receiptId);

        given(this.paymentService.findById(anyLong())).willReturn(receipt);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/receipts/{receiptId}", receiptId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount", equalTo(250)))
                .andExpect(jsonPath("$._links.self.href", equalTo("http://localhost/receipts/" + receiptId)));
    }

    @Test
    public void whenGetNonExistingThenReturnStatusNotFound() throws Exception {
        final long receiptId = 1L;
        final String message = "Receipt with id '" + receiptId + "' does not exist";

        given(this.paymentService.findById(anyLong())).willThrow(new ReceiptNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/receipts/{receiptId}", receiptId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetNonExistingThenReturnJsonError() throws Exception {
        final long receiptId = 1L;
        final String message = "Receipt with id '" + receiptId + "' does not exist";

        given(this.paymentService.findById(anyLong())).willThrow(new ReceiptNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/receipts/{receiptId}", receiptId);

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
