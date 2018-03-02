package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.Customer;
import com.example.videorentalstore.customer.CustomerNotFoundException;
import com.example.videorentalstore.customer.CustomerRepository;
import com.example.videorentalstore.customer.web.RentalItem;
import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmNotFoundException;
import com.example.videorentalstore.film.FilmRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultRentalService implements RentalService {

    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;

    public DefaultRentalService(CustomerRepository customerRepository, FilmRepository filmRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
    }

    public BigDecimal create(Long customerId, List<RentalItem> rentalItems) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with id '%d' does not exist", customerId)));

        final List<Rental> rentals = rentalItems.stream().map(rCmd -> {
            final Film film = filmRepository.findById(rCmd.getFilmId())
                    .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", rCmd.getFilmId())));
            return new Rental(film, rCmd.getDaysRented());
        }).collect(Collectors.toList());

        rentals.forEach(r -> customer.addRental(r));

        return customer.calculate();
    }
}
