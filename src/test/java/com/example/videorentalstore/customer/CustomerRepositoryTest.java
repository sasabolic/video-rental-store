package com.example.videorentalstore.customer;

import com.example.videorentalstore.film.FilmRepository;
import com.example.videorentalstore.rental.Rental;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Test
    public void whenRentalsAddedThenRentalsSizeIncreased() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();
        final int before = customer.getRentals().size();

        // add rentals
        customer.addRental(rental1);
        customer.addRental(rental2);

        final Customer result = customerRepository.save(customer);

        assertThat(result).isNotNull();
        assertThat(result.getRentals()).hasSize(before + 2);
    }

    @Test
    public void whenRentalsAddedThenCalculateExtraChargesReturnsAmountZero() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();

        // add rentals
        customer.addRental(rental1);
        customer.addRental(rental2);

        final Customer result = customerRepository.save(customer);

        final BigDecimal extraCharges = result.calculateExtraCharges();

        assertThat(extraCharges).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void whenRentalsAddedThenCalculateReturnsCorrectAmount() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();
        customer.addRental(rental1);
        customer.addRental(rental2);

        final Customer result = customerRepository.save(customer);

        final BigDecimal amount = result.calculate();

        assertThat(amount).isEqualByComparingTo(BigDecimal.valueOf(210));
    }

    @Test
    public void whenSaveThenSizeIncreased() {
        Long before = customerRepository.count();

        customerRepository.save(CustomerDataFixtures.customer());

        Long after = customerRepository.count();

        assertThat(after).isEqualTo(before.intValue() + 1);
    }

    @Test
    public void whenSaveThenSearchAllContainsSavedResult() {
        Long before = customerRepository.count();

        Customer customer = customerRepository.save(CustomerDataFixtures.customer());

        List<Customer> result = customerRepository.findAll();

        assertThat(result).hasSize(before.intValue() + 1);
        assertThat(result).contains(customer);
    }

    @Test
    public void whenSaveThenReturnCorrectResult() {
        final Customer customer = CustomerDataFixtures.customer();

        final Customer result = customerRepository.save(customer);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getBonusPoints()).isEqualTo(customer.getBonusPoints());
        assertThat(result.getRentals()).isEqualTo(customer.getRentals());
    }

    @Test
    public void whenDeactivateThenActiveFalse() {
        Customer customer = CustomerDataFixtures.customer();

        customer.deactivate();

        final Customer result = customerRepository.save(customer);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    public void whenSearchByIdThenReturnCorrectResult() {
        final Customer customer = CustomerDataFixtures.customer();
        entityManager.persist(customer);
        entityManager.flush();

        final Customer result = customerRepository.findById(customer.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getBonusPoints()).isEqualTo(customer.getBonusPoints());
        assertThat(result.getRentals()).isEqualTo(customer.getRentals());
    }

    @Test
    public void whenSearchByNameThenReturnCorrectResult() {
        final List<Customer> result = customerRepository.findByName("tes");

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Customer::getLastName).containsExactly("Tesla");
    }

    @Test
    public void whenSearchByNameThenReturnListContainingThatName() {
        final List<Customer> result = customerRepository.findByName("tes");

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Customer::getLastName).containsExactly("Tesla");
    }

    @Test
    public void whenSearchByNonExistingNameThenReturnEmptyList() {
        final List<Customer> result = customerRepository.findByName("non-existing");

        assertThat(result).isEmpty();
    }

    @Test
    public void whenSearchAllThenReturnResult() {
        final List<Customer> result = customerRepository.findAll();

        assertThat(result).isNotEmpty();
    }
}
