package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalService;
import com.example.videorentalstore.rental.web.dto.RentalResponse;
import com.example.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO: 3/9/18 Delete this endpoint
@RestController
public class RentalController {

    private final RentalService rentalService;
    private final RentalResponseAssembler rentalAssembler;

    public RentalController(RentalService rentalService, RentalResponseAssembler rentalAssembler) {
        this.rentalService = rentalService;
        this.rentalAssembler = rentalAssembler;
    }

    // TODO: 3/9/18 Remove customer id?
    @GetMapping("/rentals")
    public ResponseEntity<List<RentalResponse>> getAll(@PathVariable("id") long customerId) {
        final List<Rental> rentals = this.rentalService.findAll();

        return ResponseEntity.ok(rentalAssembler.of(rentals));
    }
}
