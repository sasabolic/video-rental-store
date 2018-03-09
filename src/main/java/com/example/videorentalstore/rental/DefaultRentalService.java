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

    public List<Rental> findAll() {
        return this.rentalRepository.findAll();
    }

    public List<Rental> findAllActiveForCustomer(Long customerId) {
        return findAllForCustomer(customerId, Rental.Status.ACTIVE);
    }

    public List<Rental> findAllForCustomer(Long customerId, Rental.Status status) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        if (status == null) {
            return customer.getRentals();
        }
        return customer.getRentals().stream().filter(r -> status == r.getStatus()).collect(Collectors.toList());
    }

    @Override
    public Receipt create(CreateRentalsCommand createRentalsCommand) {
        Customer customer = customerRepository.findById(createRentalsCommand.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", createRentalsCommand.getCustomerId())));

        List<Exception> exceptions = new ArrayList<>();
        createRentalsCommand.getCreateRentalCommands().stream()
                .forEach(createRentalCommand -> {
                    try {
                        final Film film = filmRepository.findById(createRentalCommand.getFilmId())
                                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", createRentalCommand.getFilmId())));

                        customer.addRental(new Rental(film, createRentalCommand.getDaysRented()));
                    } catch (FilmNotFoundException ex) {
                        exceptions.add(ex);
                    }

                });

        if (!exceptions.isEmpty()) {
            throw new RentalException("Could not create rentals", exceptions);
        }

        customerRepository.save(customer);

        return new Receipt(customer.calculate(), customer.getRentals());
    }

    @Override
    public Receipt returnBack(ReturnRentalsCommand returnRentalsCommand) {
        Customer customer = customerRepository.findById(returnRentalsCommand.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", returnRentalsCommand.getCustomerId())));

        List<Exception> exceptions = new ArrayList<>();
        returnRentalsCommand.getReturnRentalCommands().stream()
                .forEach(returnRentalCommand -> {
                    try {
                        final Rental rental = customer.getRentals().stream()
                                .filter(r -> returnRentalCommand.getRentalId().equals(r.getId()))
                                .findAny()
                                .orElseThrow(() -> new RentalNotFoundException(String.format("Rental with id '%d' is not rented by customer with id '%d'", returnRentalCommand.getRentalId(), returnRentalsCommand.getCustomerId())));

                        rental.markReturned();
                    } catch(RentalNotFoundException | IllegalStateException ex) {
                        exceptions.add(ex);
                    }

                });

        if (!exceptions.isEmpty()) {
            throw new RentalException("Could not return rentals", exceptions);
        }

        customerRepository.save(customer);

        return new Receipt(customer.calculateExtraCharges(), customer.getRentals());

    }
}
