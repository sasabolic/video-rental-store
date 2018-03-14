package com.example.videorentalstore.customer.web;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerDataFixtures;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].last_name", equalTo("Smith")))
                .andExpect(jsonPath("$[1].last_name", equalTo("Smith")))
                .andExpect(jsonPath("$[2].last_name", equalTo("Smith")));
    }

    @Test
    public void whenGetAllByNonExistingNameThenReturnEmptyJsonList() throws Exception {
        final String name = "non-existing";

        given(this.customerService.findAll(name)).willReturn(Collections.emptyList());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers?name={name}", name)
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void whenGetByIdThenReturnStatusOK() throws Exception {
        final long customerId = 1L;
        final Customer customer = spy(CustomerDataFixtures.customer());

        given(customer.getId()).willReturn(customerId);
        given(this.customerService.findById(anyLong())).willReturn(customer);

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
        final long customerId = 1L;
        final Customer customer = spy(CustomerDataFixtures.customer(firstName, lastName));

        given(customer.getId()).willReturn(customerId);
        given(this.customerService.findById(anyLong())).willReturn(customer);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/customers/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenCreateThenReturnStatusCreated() throws Exception {
        final long customerId = 1L;
        final Customer customer = spy(CustomerDataFixtures.customer());

        given(customer.getId()).willReturn(customerId);
        given(this.customerService.save(anyString(), anyString())).willReturn(customer);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers")
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateThenReturnLocationHeader() throws Exception {
        final long customerId = 1L;
        final Customer customer = spy(CustomerDataFixtures.customer());

        given(customer.getId()).willReturn(customerId);
        given(this.customerService.save(anyString(), anyString())).willReturn(customer);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers")
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/customers/" + customerId));
    }

    @Test
    public void whenCreateEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers")
                .content("{}")
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
    public void whenUpdateThenReturnStatusOK() throws Exception {
        given(this.customerService.update(anyLong(), anyString(), anyString()))
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
        final long customerId = 1L;
        final Customer customer = spy(CustomerDataFixtures.customer(firstName, lastName));

        given(customer.getId()).willReturn(customerId);
        given(this.customerService.update(anyLong(), anyString(), anyString()))
                .willReturn(customer);

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", customerId)
                .content(CustomerDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.first_name", equalTo(firstName)))
                .andExpect(jsonPath("$.last_name", equalTo(lastName)));
    }

    @Test
    public void whenUpdateNonExistingThenReturnStatusNotFound() throws Exception {
        final Long customerId = 1L;
        final String message = "Customer with id '" + customerId + "' does not exist";

        given(this.customerService.update(anyLong(), anyString(), anyString())).willThrow(new CustomerNotFoundException(message));

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

        given(this.customerService.update(anyLong(), anyString(), anyString())).willThrow(new CustomerNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", customerId)
                .content(CustomerDataFixtures.json())
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
    public void whenUpdateEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", 1L)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/{customerId}", 1L)
                .content("{}")
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }
}