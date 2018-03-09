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
    public List<Rental> findAllForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        return customer.getRentals();
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
    public Receipt process(BatchRentalCommand batchRentalCommand) {
        Customer customer = customerRepository.findById(batchRentalCommand.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", batchRentalCommand.getCustomerId())));

        List<Exception> exceptions = new ArrayList<>();
        batchRentalCommand.getRentalCommands().stream()
                .forEach(rentalCommand -> {
                    try {
                        final Rental rental = customer.getRentals().stream()
                                .filter(r -> rentalCommand.getRentalId().equals(r.getId()))
                                .findAny()
                                .orElseThrow(() -> new RentalNotFoundException(String.format("Rental with id '%d' is not rented by customer with id '%d'", rentalCommand.getRentalId(), batchRentalCommand.getCustomerId())));

                        switch (batchRentalCommand.getAction()) {
                            case PAY:
                                rental.markActive();
                                break;
                            case RETURN:
                                rental.markExtraPaymentExpected();
                                break;
                            case EXTRA_PAY:
                                rental.markCompleted();
                        }
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
