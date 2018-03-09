package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.*;
import com.example.videorentalstore.rental.web.dto.*;
import com.example.videorentalstore.rental.web.dto.assembler.ReceiptResponseAssembler;
import com.example.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/customers/{id}/rentals")
    public ResponseEntity<List<RentalResponse>> getAll(@PathVariable("id") long customerId, @RequestParam(required = false) String status) {
        final List<Rental> rentals = this.rentalService.findAllForCustomer(customerId);

        return ResponseEntity.ok(rentalResponseAssembler.of(rentals));
    }

    @PostMapping("/customers/{id}/rentals")
    public ResponseEntity<ReceiptResponse> create(@PathVariable("id") long customerId, @RequestBody @Valid CreateRentalRequestList createRentalRequestList) {
        CreateRentalsCommand createRentalsCommand = new CreateRentalsCommand(customerId, createRentalRequestList.stream().map(CreateRentalRequest::toCreateRentalCommand).collect(Collectors.toList()));

        final Receipt receipt = rentalService.create(createRentalsCommand);

        return ResponseEntity.ok(receiptResponseAssembler.of(receipt));
    }

    @PatchMapping("/customers/{id}/rentals")
    public ResponseEntity<ReceiptResponse> patch(@PathVariable("id") long customerId, @RequestBody @Valid BatchRentalRequest batchRentalRequest) {
        BatchRentalCommand batchRentalCommand = new BatchRentalCommand(customerId, BatchRentalCommand.Action.valueOf(batchRentalRequest.getAction()), batchRentalRequest.getRentals().stream().map(RentalRequest::toRentalCommand).collect(Collectors.toList()));

        final Receipt receipt = this.rentalService.process(batchRentalCommand);

        return ResponseEntity.ok(receiptResponseAssembler.of(receipt));
    }
}
