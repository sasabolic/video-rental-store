package io.sixhours.videorentalstore.customer;

import io.sixhours.videorentalstore.film.FilmRepository;
import io.sixhours.videorentalstore.rental.Rental;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

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

    private Customer customer;

    @Before
    public void setUp() {
        customer = CustomerDataFixtures.customer("John", "Smith");
        entityManager.persist(customer);
        entityManager.flush();
    }

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
        final Customer result = customerRepository.save(customer);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getBonusPoints()).isEqualTo(customer.getBonusPoints());
        assertThat(result.getRentals()).isEqualTo(customer.getRentals());
    }

    @Test
    public void whenDeactivateThenActiveFalse() {
        customer.deactivate();

        final Customer result = customerRepository.save(customer);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    public void whenDeactivateThenSizeDecreased() {
        Long before = customerRepository.count();

        customer.deactivate();

        customerRepository.save(customer);

        Long result = customerRepository.count();

        assertThat(result).isEqualTo(before.intValue() - 1);
    }

    @Test
    public void whenDeactivateThenSearchAllReturnsCorrectResult() {
        customer.deactivate();

        customerRepository.save(customer);

        final List<Customer> result = customerRepository.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result).extracting(Customer::isActive).containsOnly(true);
    }

    @Test
    public void whenDeactivateThenSearchByIdReturnsCorrectResult() {
        customer.deactivate();

        customerRepository.save(customer);

        final Optional<Customer> result = customerRepository.findById(customer.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void whenDeactivateThenSearchByNameReturnsCorrectResult() {
        customer.deactivate();

        customerRepository.save(customer);

        final List<Customer> result = customerRepository.findByName("john");

        assertThat(result).isEmpty();
    }

    @Test
    public void whenSearchByIdThenReturnCorrectResult() {

        final Customer result = customerRepository.findById(customer.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(result.getLastName()).isEqualTo(customer.getLastName());
        assertThat(result.getBonusPoints()).isEqualTo(customer.getBonusPoints());
        assertThat(result.getRentals()).isEqualTo(customer.getRentals());
    }

    @Test
    public void whenSearchByIdThenActiveTrue() {

        final Customer result = customerRepository.findById(customer.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.isActive()).isTrue();
    }

    @Test
    public void givenReturnedRentalsWhenSearchByIdRentalsEmpty() {
        final List<Rental> rentals = customerRepository.findById(customer.getId()).get().getRentals();

        rentals.stream().map(Rental::markReturned).forEach(r -> customer.addRental(r));

        customerRepository.save(customer);

        final Customer result = customerRepository.findById(customer.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.isActive()).isTrue();
        assertThat(result.getRentals()).isEmpty();
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
