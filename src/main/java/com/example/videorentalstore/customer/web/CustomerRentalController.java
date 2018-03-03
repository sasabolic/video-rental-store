package com.example.videorentalstore.customer.web;

import com.example.videorentalstore.customer.Invoice;
import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.ReleaseType;
import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@RestController
public class CustomerRentalController {

    private final RentalService rentalService;

    public CustomerRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/customers/{id}/rentals")
    public List<Rental> get(@PathVariable("id") long customerId) {
        final Instant startDate = Instant.now().minus(3, ChronoUnit.DAYS);
        return Arrays.asList(
                new Rental(new Film("Matrix 11", ReleaseType.NEW_RELEASE), 1, startDate),
                new Rental(new Film("Spider Man", ReleaseType.REGULAR_RELEASE), 5, startDate),
                new Rental(new Film("Spider Man 2", ReleaseType.REGULAR_RELEASE), 2, startDate),
                new Rental(new Film("Out of Africa", ReleaseType.OLD_RELEASE), 7, startDate));
    }

    @PostMapping("/customers/{id}/rentals")
    public ResponseEntity<Invoice> create(@PathVariable("id") long customerId, @RequestBody List<RentalItem> rentalItems) {

        final BigDecimal amount = rentalService.create(customerId, rentalItems);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().build().toUri();

        return ResponseEntity.created(location).body(new Invoice(amount));
    }



    @PatchMapping("/customers/{id}/rentals")
    public ResponseEntity<Void> update(@RequestBody List<Rental> rentals) {
        return null;
    }

}
