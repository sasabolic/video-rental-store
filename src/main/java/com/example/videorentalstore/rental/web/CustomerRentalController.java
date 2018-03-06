package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CustomerRentalController {

    private final RentalService rentalService;

    public CustomerRentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/customers/{id}/rentals")
    public ResponseEntity<List<Rental>> getAll(@PathVariable("id") long customerId) {
        final List<Rental> rentals = this.rentalService.findAllRentedForCustomer(customerId);

        return ResponseEntity.ok(rentals);
    }

    @PostMapping("/customers/{id}/rentals")
    public ResponseEntity<Receipt> create(@PathVariable("id") long customerId, @RequestBody List<CreateRentalRequest> createRentalRequests) {
        CreateRentalsCommand createRentalsCommand = new CreateRentalsCommand(customerId, createRentalRequests.stream().map(r -> r.toCreateRentalCommand()).collect(Collectors.toList()));

        final Receipt response = rentalService.create(createRentalsCommand);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/customers/{id}/rentals")
    public ResponseEntity<Receipt> update(@PathVariable("id") long customerId, @RequestBody List<Long> rentalIds) {
        ReturnRentalsCommand returnRentalsCommand = new ReturnRentalsCommand(customerId, rentalIds);

        final Receipt response = this.rentalService.returnBack(returnRentalsCommand);

        return ResponseEntity.ok(response);
    }
}
