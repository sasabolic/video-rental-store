package com.example.videorentalstore.customer.web;

import com.example.videorentalstore.customer.*;
import com.example.videorentalstore.customer.web.dto.assembler.CustomerResponseAssembler;
import com.example.videorentalstore.customer.web.dto.assembler.DefaultCustomerResponseAssembler;
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

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = CustomerController.class)
public class CustomerControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public CustomerResponseAssembler customerResponseAssembler() {
            return new DefaultCustomerResponseAssembler();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void whenGetAllThenReturnStatusOK() throws Exception {
        given(this.customerService.findAll(null))
                .willReturn(CustomerDataFixtures.customers());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllThenReturnJsonList() throws Exception {
        given(this.customerService.findAll(null))
                .willReturn(CustomerDataFixtures.customers());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].first_name", equalTo("John")))
                .andExpect(jsonPath("$[0].last_name", equalTo("Smith")))
                .andExpect(jsonPath("$[1].first_name", equalTo("Giovanni")))
                .andExpect(jsonPath("$[1].last_name", equalTo("Smith")))
                .andExpect(jsonPath("$[2].first_name", equalTo("Evan")))
                .andExpect(jsonPath("$[2].last_name", equalTo("Smith")));
    }

    @Test
    public void whenGetAllByNameThenReturnJsonListContainingThatName() throws Exception {
        final String name = "smith";

        given(this.customerService.findAll(name)).willReturn(CustomerDataFixtures.customers());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers?name={name}", name)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].last_name", equalTo("Smith")))
                .andExpect(jsonPath("$[1].last_name", equalTo("Smith")))
                .andExpect(jsonPath("$[2].last_name", equalTo("Smith")));
    }

    @Test
    public void whenGetAllByNonExistingNameThenReturnEmptyJsonList() throws Exception {
        final String name = "smith";

        given(this.customerService.findAll(name)).willReturn(Collections.emptyList());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers?name={name}", name)
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void whenGetByIdThenReturnStatusOK() throws Exception {
        given(this.customerService.findById(anyLong())).willReturn(CustomerDataFixtures.customer());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}", 1L)
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetByIdThenReturnJson() throws Exception {
        final String firstName = "John";
        final String lastName = "Smith";

        given(this.customerService.findById(anyLong())).willReturn(CustomerDataFixtures.customer(firstName, lastName));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}", 1L)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.first_name", equalTo(firstName)))
                .andExpect(jsonPath("$.last_name", equalTo(lastName)));
    }

    @Test
    public void whenGetNonExistingThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.findById(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetNonExistingThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.findById(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}", customerId)
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
    public void whenCreateThenReturnStatusOK() throws Exception {
        given(this.customerService.save(isA(CreateCustomerCommand.class)))
				.willReturn(CustomerDataFixtures.customer());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers")
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateThenReturnJson() throws Exception {
        final String firstName = "John";
        final String lastName = "Smith";

        given(this.customerService.save(isA(CreateCustomerCommand.class)))
                .willReturn(CustomerDataFixtures.customer(firstName, lastName));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers")
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.first_name", equalTo(firstName)))
                .andExpect(jsonPath("$.last_name", equalTo(lastName)));
    }

    @Test
    public void whenUpdateThenReturnStatusOK() throws Exception {
        given(this.customerService.update(isA(UpdateCustomerCommand.class)))
                .willReturn(CustomerDataFixtures.customer());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", 1L)
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateThenReturnJson() throws Exception {
        final String firstName = "John";
        final String lastName = "Smith";

        given(this.customerService.update(isA(UpdateCustomerCommand.class)))
                .willReturn(CustomerDataFixtures.customer(firstName, lastName));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", 1L)
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.first_name", equalTo(firstName)))
                .andExpect(jsonPath("$.last_name", equalTo(lastName)));
    }

    @Test
    public void whenUpdateNonExistingThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.update(isA(UpdateCustomerCommand.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", customerId)
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateNonExistingThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.update(isA(UpdateCustomerCommand.class))).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", customerId)
                .content(CustomerDataFixtures.json())
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
    public void whenDeleteThenReturnStatusNoContent() throws Exception {
        final Long customerId = 1L;

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/customers/{customerId}", customerId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeleteThenReturnEmptyBody() throws Exception {
        final Long customerId = 1L;

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/customers/{customerId}", customerId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void whenDeleteNonExistingThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.delete(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/customers/{customerId}", customerId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteNonExistingThenReturnJsonError() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.delete(anyLong())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/customers/{customerId}", customerId);

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