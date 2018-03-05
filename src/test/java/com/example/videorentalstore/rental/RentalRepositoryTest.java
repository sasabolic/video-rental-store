package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.film.FilmRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RentalRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    public void whenRentalsAddedThenRentalsSizeIsCorrect() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();

        // add rentals
        customer.addRental(rental1);
        customer.addRental(rental2);

        customerRepository.save(customer);

        final Iterable<Rental> rentals = rentalRepository.findAllByCustomerIdAndStatus(customer.getId(), Rental.Status.RENTED);

        assertThat(rentals).isNotNull();
        assertThat(rentals).isNotEmpty();
        assertThat(rentals).hasSize(2);
    }

    @Test
    public void whenRentalsAddedThenRentalsSizeWithStatusRentedIsZero() {
        Rental rental1 = new Rental(filmRepository.findById(1L).get(), 3);
        Rental rental2 = new Rental(filmRepository.findById(2L).get(), 5);

        final Customer customer = customerRepository.findById(1L).get();

        // add rentals
        customer.addRental(rental1);
        customer.addRental(rental2);

        customerRepository.save(customer);

        final Iterable<Rental> rentals = rentalRepository.findAllByCustomerIdAndStatus(customer.getId(), Rental.Status.RETURNED);

        assertThat(rentals).isNotNull();
        assertThat(rentals).isEmpty();
    }

}
