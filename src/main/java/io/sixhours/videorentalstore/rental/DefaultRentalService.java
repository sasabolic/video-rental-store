package io.sixhours.videorentalstore.rental;

import io.sixhours.videorentalstore.customer.Customer;
import io.sixhours.videorentalstore.customer.CustomerNotFoundException;
import io.sixhours.videorentalstore.customer.CustomerRepository;
import io.sixhours.videorentalstore.film.Film;
import io.sixhours.videorentalstore.film.FilmNotFoundException;
import io.sixhours.videorentalstore.film.FilmRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link RentalService}.
 *
 * @author Sasa Bolic
 */
@Service
@Transactional
public class DefaultRentalService implements RentalService {

    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;

    public DefaultRentalService(CustomerRepository customerRepository, FilmRepository filmRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public List<Rental> findAllForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        return customer.getRentals();
    }

    @Override
    public BatchRental create(Long customerId, List<RentalInfo> rentalInfos) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        if (!customer.getRentals().isEmpty()) {
            throw new RentalException(String.format("Could not create new rentals. Customer with '%d' contains active rentals.", customerId));
        }

        List<Exception> exceptions = new ArrayList<>();
        rentalInfos.forEach(rentalInfo -> {
                    try {
                        final Film film = filmRepository.findById(rentalInfo.getFilmId())
                                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", rentalInfo.getFilmId())));

                        customer.addRental(new Rental(film, rentalInfo.getDaysRented()));
                    } catch (FilmNotFoundException ex) {
                        exceptions.add(ex);
                    }

                });

        if (!exceptions.isEmpty()) {
            throw new RentalException("Could not create new rentals", exceptions);
        }

        customer.addBonusPoints();
        customerRepository.save(customer);

        return new BatchRental(customer.calculatePrice(), customer.getRentals());
    }

    @Override
    public BatchRental returnBack(Long customerId, List<Long> rentalIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        List<Exception> exceptions = new ArrayList<>();
        rentalIds.forEach(rentalId -> {
                    try {
                        final Rental rental = customer.getRentals().stream()
                                .filter(r -> rentalId.equals(r.getId()))
                                .findAny()
                                .orElseThrow(() -> new RentalNotFoundException(String.format("Rental with id '%d' is not rented by customer with id '%d'", rentalId, customer.getId())));

                        rental.markReturned();
                    } catch (RentalNotFoundException | IllegalStateException ex) {
                        exceptions.add(ex);
                    }
                });

        if (!exceptions.isEmpty()) {
            throw new RentalException("Could not return back rentals", exceptions);
        }

        customerRepository.save(customer);

        return new BatchRental(customer.calculateExtraCharges(), customer.getRentals());
    }
}
