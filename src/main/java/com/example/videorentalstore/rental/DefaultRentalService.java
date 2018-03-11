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
    public Receipt create(BatchRentalCreateCommand batchRentalCreateCommand) {
        Customer customer = customerRepository.findById(batchRentalCreateCommand.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", batchRentalCreateCommand.getCustomerId())));

        List<Exception> exceptions = new ArrayList<>();
        batchRentalCreateCommand.getRentalInfos().stream()
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

        return new Receipt(customer.calculatePrice(), customer.getRentals());
    }

    @Override
    public List<Rental> create(Long customerId, List<RentalInfo> rentalInfos) {
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

        return customer.getRentals();
    }

    @Override
    public List<Rental> returnBack(Long customerId, List<Long> rentalIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        List<Exception> exceptions = new ArrayList<>();
        rentalIds.stream()
                .forEach(rentalId -> {
                    try {
                        final Rental rental = checkRentalExistsForCustomer(customer, rentalId);

                        rental.markReturned();
                        rental.markLatePaymentExpected();
                    } catch (RentalNotFoundException | IllegalStateException ex) {
                        exceptions.add(ex);
                    }
                });

        if (!exceptions.isEmpty()) {
            throw new RentalException("Could not process batch rental command", exceptions);
        }

        customerRepository.save(customer);

        return customer.getRentals();
    }

    @Override
    public void delete(Long customerId, List<Long> ids) {

    }

    private Rental checkRentalExistsForCustomer(Customer customer, Long rentalId) {
        return customer.getRentals().stream()
                .filter(r -> rentalId.equals(r.getId()))
                .findAny()
                .orElseThrow(() -> new RentalNotFoundException(String.format("Rental with id '%d' is not rented by customer with id '%d'", rentalId, customer.getId())));
    }
}
