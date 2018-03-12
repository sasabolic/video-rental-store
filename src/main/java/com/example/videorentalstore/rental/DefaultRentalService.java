package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmNotFoundException;
import com.example.videorentalstore.film.FilmRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class DefaultRentalService implements RentalService {

    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;
    private final RentalRepository rentalRepository;

    public DefaultRentalService(CustomerRepository customerRepository, FilmRepository filmRepository, RentalRepository rentalRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public List<Rental> findAll() {
        return this.rentalRepository.findAll();
    }

    @Override
    public List<Rental> findAllForCustomer(Long customerId, Rental.Status status) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        if (status == null) {
            return customer.getRentals();
        }

        return customer.getRentals().stream().filter(r -> r.hasStatus(status)).collect(Collectors.toList());
    }

    @Override
    public RentalResult create(Long customerId, List<RentalInfo> rentalInfos) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        List<Exception> exceptions = new ArrayList<>();
        rentalInfos.stream()
                .forEach(rentalInfo -> {
                    try {
                        final Film film = filmRepository.findById(rentalInfo.getFilmId())
                                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", rentalInfo.getFilmId())));

                        customer.addRental(new Rental(film, rentalInfo.getDaysRented()));
                    } catch (FilmNotFoundException ex) {
                        exceptions.add(ex);
                    }

                });

        if (!exceptions.isEmpty()) {
            throw new RentalException("Could not create rentals", exceptions);
        }

        customerRepository.save(customer);

        return new RentalResult(Rental.Status.UP_FRONT_PAYMENT_EXPECTED, customer.getRentals());
    }

    @Override
    public RentalResult returnBack(Long customerId, List<Long> rentalIds) {
        final Customer customer = process(customerId, rentalIds, r -> r.markReturned().markLatePaymentExpected(), "Could not return back rentals");

        return new RentalResult(Rental.Status.LATE_PAYMENT_EXPECTED, customer.getRentals());
    }

    @Override
    public void delete(Long customerId, List<Long> rentalIds) {
        final Customer customer = process(customerId, rentalIds, Rental::deactivate, "Could not delete rentals");

        rentalRepository.deleteAll(customer.getRentals());
    }

    private Customer process(Long customerId, List<Long> rentalIds, Function<Rental, Rental> func, String errorMsg) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        List<Exception> exceptions = new ArrayList<>();
        rentalIds.stream()
                .forEach(rentalId -> {
                    try {
                        final Rental rental = checkRentalExistsForCustomer(customer, rentalId);

                        rental.apply(func);
                    } catch (RentalNotFoundException | IllegalStateException ex) {
                        exceptions.add(ex);
                    }
                });

        if (!exceptions.isEmpty()) {
            throw new RentalException(errorMsg, exceptions);
        }

        customerRepository.save(customer);

        return customer;
    }

    private Rental checkRentalExistsForCustomer(Customer customer, Long rentalId) {
        return customer.getRentals().stream()
                .filter(r -> rentalId.equals(r.getId()))
                .findAny()
                .orElseThrow(() -> new RentalNotFoundException(String.format("Rental with id '%d' is not rented by customer with id '%d'", rentalId, customer.getId())));
    }
}
