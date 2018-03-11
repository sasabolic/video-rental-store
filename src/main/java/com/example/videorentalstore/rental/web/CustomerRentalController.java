package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.Rental;
import com.example.videorentalstore.rental.RentalService;
import com.example.videorentalstore.rental.web.dto.*;
import com.example.videorentalstore.rental.web.dto.assembler.ReceiptResponseAssembler;
import com.example.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
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
    private final ReceiptResponseAssembler receiptResponseAssembler;

    public CustomerRentalController(RentalService rentalService, RentalResponseAssembler rentalResponseAssembler, ReceiptResponseAssembler receiptResponseAssembler) {
        this.rentalService = rentalService;
        this.rentalResponseAssembler = rentalResponseAssembler;
        this.receiptResponseAssembler = receiptResponseAssembler;
    }

    @GetMapping("/customers/{customerId}/rentals")
    public ResponseEntity<List<RentalResponse>> getAll(@PathVariable("customerId") long customerId, @RequestParam(required = false) String status) {
        final List<Rental> rentals = this.rentalService.findAllForCustomer(customerId, status != null ? Rental.Status.valueOf(status) : null);

        return ResponseEntity.ok(rentalResponseAssembler.of(rentals));
    }

    @PostMapping("/customers/{customerId}/rentals")
    public ResponseEntity<List<RentalResponse>> create(@PathVariable("customerId") long customerId, @RequestBody @Valid CreateRentalRequestList createRentalRequestList) {
        final List<Rental> rentals = rentalService.create(customerId, createRentalRequestList.stream().map(CreateRentalRequest::toRentalInfo).collect(Collectors.toList()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("status={status}")
                .buildAndExpand(Rental.Status.UP_FRONT_PAYMENT_EXPECTED).toUri();

//        return ResponseEntity.ok(rentalResponseAssembler.of(rentals));
        return ResponseEntity.created(location).body(rentalResponseAssembler.of(rentals));
    }


    @PatchMapping("/customers/{customerId}/rentals")
    public ResponseEntity<List<RentalResponse>> returnBack(@PathVariable("customerId") long customerId, @RequestBody @Valid BatchReturnRentalRequest batchReturnRentalRequest) {
        final List<Rental> rentals = this.rentalService.returnBack(customerId, batchReturnRentalRequest.getReturnRentalRequests().stream().map(ReturnRentalRequest::getRentalId).collect(Collectors.toList()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("status={status}")
                .buildAndExpand(Rental.Status.LATE_PAYMENT_EXPECTED).toUri();

//        return ResponseEntity.ok(rentalResponseAssembler.of(rentals));
        return ResponseEntity.ok(rentalResponseAssembler.of(rentals));
    }

    @DeleteMapping("/customers/{customerId}/rentals")
    public ResponseEntity<Void> delete(@PathVariable("customerId") long customerId, @RequestParam List<Long> ids) {
        return ResponseEntity.noContent().build();
    }
}
