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

        final Customer saved = customerRepository.save(customer);

        assertThat(saved).isNotNull();
        assertThat(saved.getRentals()).hasSize(before + 2);
    }

    @Test
    public void whenRentalsAddedThenCalculateExtraChargesReturnsZeroResult() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();

        // add rentals
        customer.addRental(rental1);
        customer.addRental(rental2);

        final Customer saved = customerRepository.save(customer);

        final BigDecimal extraCharges = saved.calculateExtraCharges();

        assertThat(extraCharges).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void whenRentalsAddedThenCalculateReturnsResult() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();
        customer.addRental(rental1);
        customer.addRental(rental2);

        final Customer saved = customerRepository.save(customer);

        final BigDecimal amount = saved.calculate();

        assertThat(amount).isEqualByComparingTo(BigDecimal.valueOf(210));
    }

    @Test
    public void whenCreateNewCustomerThenReturnResult() {

        Long before = customerRepository.count();

        Customer customer = customerRepository.save(CustomerDataFixtures.customer());

        Iterable<Customer> result = customerRepository.findAll();

        assertThat(result).hasSize(before.intValue() + 1);
        assertThat(result).contains(customer);
    }

    @Test
    public void whenSearchAllThenReturnResult() {

        final Iterable<Customer> customers = customerRepository.findAll();

        assertThat(customers).isNotEmpty();
    }


}
