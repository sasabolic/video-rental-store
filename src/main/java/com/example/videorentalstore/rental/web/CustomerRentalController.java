package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalService;
import com.example.videorentalstore.rental.RentalResult;
import com.example.videorentalstore.rental.web.dto.*;
import com.example.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST customer's rental resources.
 */
@RestController
public class CustomerRentalController {

    private final RentalService rentalService;
    private final RentalResponseAssembler rentalResponseAssembler;

    public CustomerRentalController(RentalService rentalService, RentalResponseAssembler rentalResponseAssembler) {
        this.rentalService = rentalService;
        this.rentalResponseAssembler = rentalResponseAssembler;
    }

    @GetMapping("/customers/{customerId}/rentals")
    public ResponseEntity<Resources<RentalResponse>> getAll(@PathVariable("customerId") Long customerId, @RequestParam(required = false) String status) {
        final Rental.Status statusValue = status != null ? Rental.Status.valueOf(status) : null;

        final List<Rental> rentals = this.rentalService.findAllForCustomer(customerId, statusValue);


        return ResponseEntity.ok(rentalResponseAssembler.of(rentals, statusValue, customerId));
    }

    @PostMapping("/customers/{customerId}/rentals")
    public ResponseEntity<List<RentalResponse>> create(@PathVariable("customerId") Long customerId, @RequestBody @Valid BatchCreateRentalRequest batchCreateRentalRequest) {
        final RentalResult rentalResult = rentalService.create(customerId, batchCreateRentalRequest.stream().map(CreateRentalRequest::toRentalInfo).collect(Collectors.toList()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("status={status}")
                .buildAndExpand(rentalResult.getStatus()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/customers/{customerId}/rentals")
    public ResponseEntity<Resources<RentalResponse>> returnBack(@PathVariable("customerId") Long customerId, @RequestBody @Valid BatchReturnRentalRequest batchReturnRentalRequest) {
        final RentalResult rentalResult = this.rentalService.returnBack(customerId, batchReturnRentalRequest.getReturnRentalRequests().stream().map(ReturnRentalRequest::getRentalId).collect(Collectors.toList()));

        return ResponseEntity.ok(rentalResponseAssembler.of(rentalResult.getRentals(), rentalResult.getStatus(), customerId));
    }

    @DeleteMapping("/customers/{customerId}/rentals")
    public ResponseEntity<Void> delete(@PathVariable("customerId") Long customerId, @RequestParam List<Long> ids) {
        return ResponseEntity.noContent().build();
    }
}
