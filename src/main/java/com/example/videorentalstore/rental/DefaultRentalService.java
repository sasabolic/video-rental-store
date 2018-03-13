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

    public DefaultRentalService(CustomerRepository customerRepository, FilmRepository filmRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public BatchRental findAllForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        return new BatchRental(customer.getRentals());
    }

    @Override
    public BatchRental create(Long customerId, List<RentalInfo> rentalInfos) {
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

        customer.addBonusPoints();
        customerRepository.save(customer);

        return new BatchRental(customer.calculatePrice(), customer.getRentals());
    }

    @Override
    public BatchRental returnBack(Long customerId, List<Long> rentalIds) {
//        final Customer customer = process(customerId, rentalIds, r -> r.markReturned(), "Could not return back rentals");
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        List<Exception> exceptions = new ArrayList<>();
        rentalIds.stream()
                .forEach(rentalId -> {
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

//    @Override
//    public void delete(Long customerId, List<Long> rentalIds) {
//        final Customer customer = process(customerId, rentalIds, Rental::deactivate, "Could not delete rentals");
//
//        rentalRepository.deleteAll(customer.getRentals());
//    }
//
//    private Customer process(Long customerId, List<Long> rentalIds, Function<Rental, Rental> func, String errorMsg) {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));
//
//        List<Exception> exceptions = new ArrayList<>();
//        rentalIds.stream()
//                .forEach(rentalId -> {
//                    try {
//                        final Rental rental = customer.getRentals().stream()
//                                .filter(r -> rentalId.equals(r.getId()))
//                                .findAny()
//                                .orElseThrow(() -> new RentalNotFoundException(String.format("Rental with id '%d' is not rented by customer with id '%d'", rentalId, customer.getId())));
//
//                        rental.apply(func);
//                    } catch (RentalNotFoundException | IllegalStateException ex) {
//                        exceptions.add(ex);
//                    }
//                });
//
//        if (!exceptions.isEmpty()) {
//            throw new RentalException(errorMsg, exceptions);
//        }
//
//        customerRepository.save(customer);
//
//        return customer;
//    }
}
