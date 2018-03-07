package com.example.videorentalstore.customer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class CustomerServiceTest {

    private CustomerService customerService;

    @Mock
    private static CustomerRepository customerRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        customerService = new DefaultCustomerService(customerRepository);
    }

    @Test
    public void whenFindAllThenReturnListOfCustomers() {
        doReturn(CustomerDataFixtures.customers()).when(customerRepository).findAll();

        final List<Customer> result = customerService.findAll(null);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
    }

    @Test
    public void whenFindAllByNameThenReturnListOfCustomersContainingName() {
        final String name = "smith";
        doReturn(CustomerDataFixtures.customers()).when(customerRepository).findByNameContainingIgnoreCase(name);

        final List<Customer> result = customerService.findAll(name);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Customer::getLastName).containsOnly("Smith");
    }

    @Test
    public void whenFindByIdThenReturnCustomer() {
        doReturn(Optional.of(CustomerDataFixtures.customer())).when(customerRepository).findById(isA(Long.class));

        final Customer result = customerService.findById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    public void whenFindByNonExistingIdThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(isA(Long.class));

        customerService.findById(customerId);
    }

    @Test
    public void whenCreatingCustomerThenReturnCustomer() {
        final String firstName = "Nikola";
        final String lastName = "Tesla";
        final CreateCustomerCommand createCustomerCommand = new CreateCustomerCommand(firstName, lastName);

        doReturn(CustomerDataFixtures.customer(firstName, lastName)).when(customerRepository).save(isA(Customer.class));

        final Customer result = customerService.save(createCustomerCommand);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("firstName", firstName);
        assertThat(result).hasFieldOrPropertyWithValue("lastName", lastName);
    }

    @Test
    public void whenCreatingCustomerThenActiveIsTrue() {
        final Customer customer = CustomerDataFixtures.customer();
        final CreateCustomerCommand createCustomerCommand = new CreateCustomerCommand(customer.getFirstName(), customer.getLastName());

        doReturn(customer).when(customerRepository).save(isA(Customer.class));

        final Customer result = customerService.save(createCustomerCommand);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", true);
    }

    @Test
    public void whenUpdatingCustomerThenReturnUpdatedCustomer() {
        final String firstName = "Dzoni";
        final String lastName = "Teslic";
        final String newFirstName = "Nikola";
        final String newLastName = "Tesla";

        final UpdateCustomerCommand updateCustomerCommand = new UpdateCustomerCommand(5L, newFirstName, newLastName);

        doReturn(Optional.of(CustomerDataFixtures.customer(firstName, lastName))).when(customerRepository).findById(isA(Long.class));
        doReturn(CustomerDataFixtures.customer(newFirstName, newLastName)).when(customerRepository).save(isA(Customer.class));

        final Customer result = customerService.update(updateCustomerCommand);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("firstName", newFirstName);
        assertThat(result).hasFieldOrPropertyWithValue("lastName", newLastName);
    }

    @Test
    public void whenUpdatingNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        final UpdateCustomerCommand updateCustomerCommand = new UpdateCustomerCommand(customerId, "John", "Smith");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(isA(Long.class));

        customerService.update(updateCustomerCommand);
    }

    @Test
    public void whenDeletingCustomerThenActiveIsFalse() {
        final Customer customer = CustomerDataFixtures.customer();

        doReturn(Optional.of(customer)).when(customerRepository).findById(isA(Long.class));
        doReturn(customer).when(customerRepository).save(isA(Customer.class));

        final Customer result = customerService.delete(1L);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    public void whenDeletingNonExistingCustomerThenThrowException() {
        final long customerId = 1L;

        thrown.expect(CustomerNotFoundException.class);
        thrown.expectMessage("Customer with id '" + customerId + "' does not exist");

        doReturn(Optional.ofNullable(null)).when(customerRepository).findById(isA(Long.class));

        customerService.delete(customerId);
    }
}
