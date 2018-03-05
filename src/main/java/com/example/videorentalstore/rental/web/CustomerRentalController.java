package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalResponse;
import com.example.videorentalstore.rental.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerRentalController {

    private final RentalService rentalService;

    public CustomerRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/customers/{id}/rentals")
    public List<Rental> get(@PathVariable("id") long customerId) {
        return this.rentalService.findAll(customerId);
    }

    @PostMapping("/customers/{id}/rentals")
    public ResponseEntity<RentalResponse> create(@PathVariable("id") long customerId, @RequestBody List<CreateRentalRequest> createRentalRequests) {

        final RentalResponse response = rentalService.create(customerId, createRentalRequests);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/customers/{id}/rentals")
    public ResponseEntity<RentalResponse> update(@PathVariable("id") long customerId, @RequestBody List<Long> rentalIds) {
        final RentalResponse response = this.rentalService.returnBack(customerId, rentalIds);

        return ResponseEntity.ok(response);
    }

}
