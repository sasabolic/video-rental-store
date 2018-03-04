package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.customer.web.CreateRentalRequest;
import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmNotFoundException;
import com.example.videorentalstore.film.FilmRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<Rental> findAll(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        return customer.getRentals();
    }

    @Override
    public RentalResponse create(Long customerId, List<CreateRentalRequest> createRentalRequests) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));


        createRentalRequests.stream()
                .forEach(createRentalRequest -> {
                    final Film film = filmRepository.findById(createRentalRequest.getFilmId())
                            .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", createRentalRequest.getFilmId())));

                    customer.addRental(new Rental(film, createRentalRequest.getDaysRented()));
                });


        customerRepository.save(customer);

        return new RentalResponse(customer.calculate(), customer.getRentals());
    }


    @Override
    public RentalResponse returnBack(Long customerId, List<Long> rentalIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));


        customer.getRentals().stream()
                .filter(r -> rentalIds.contains(r.getId()))
                .forEach(Rental::markReturned);

        customerRepository.save(customer);

        return new RentalResponse(customer.calculateExtraCharges(), customer.getRentals());

    }


}
