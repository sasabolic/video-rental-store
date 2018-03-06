package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/rentals")
    public ResponseEntity<List<Rental>> getAll(@PathVariable("id") long customerId) {
        final List<Rental> rentals = this.rentalService.findAll();

        return ResponseEntity.ok(rentals);
    }
}
