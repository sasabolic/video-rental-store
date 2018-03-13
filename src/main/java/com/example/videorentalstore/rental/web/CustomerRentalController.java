package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.BatchRental;
import com.example.videorentalstore.rental.RentalService;
import com.example.videorentalstore.rental.web.dto.BatchCreateRentalRequest;
import com.example.videorentalstore.rental.web.dto.BatchRentalReponse;
import com.example.videorentalstore.rental.web.dto.BatchReturnRentalRequest;
import com.example.videorentalstore.rental.web.dto.ReturnRentalRequest;
import com.example.videorentalstore.rental.web.dto.assembler.BatchRentalResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * REST customer's rental resources.
 */
@RestController
public class CustomerRentalController {

    private final RentalService rentalService;
    private final BatchRentalResponseAssembler batchRentalResponseAssembler;

    public CustomerRentalController(RentalService rentalService, BatchRentalResponseAssembler batchRentalResponseAssembler) {
        this.rentalService = rentalService;
        this.batchRentalResponseAssembler = batchRentalResponseAssembler;
    }

    @GetMapping("/customers/{customerId}/rentals")
    public ResponseEntity<BatchRentalReponse> getAll(@PathVariable("customerId") Long customerId, @RequestParam(required = false) String status) {
        final BatchRental batchRental = this.rentalService.findAllForCustomer(customerId);

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }

    @PostMapping("/customers/{customerId}/rentals")
    public ResponseEntity<BatchRentalReponse> create(@PathVariable("customerId") Long customerId, @RequestBody @Valid BatchCreateRentalRequest batchCreateRentalRequest) {
        final BatchRental batchRental = rentalService.create(customerId, batchCreateRentalRequest.toRentalInfoList());

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }

    @PatchMapping("/customers/{customerId}/rentals")
    public ResponseEntity<BatchRentalReponse> returnBack(@PathVariable("customerId") Long customerId, @RequestBody @Valid BatchReturnRentalRequest batchReturnRentalRequest) {
        final BatchRental batchRental = this.rentalService.returnBack(customerId, batchReturnRentalRequest.getReturnRentalRequests().stream().map(ReturnRentalRequest::getRentalId).collect(Collectors.toList()));

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }
}
