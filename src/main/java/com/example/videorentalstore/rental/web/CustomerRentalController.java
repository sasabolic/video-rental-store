package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.BatchRental;
import com.example.videorentalstore.rental.RentalService;
import com.example.videorentalstore.rental.web.dto.BatchCreateRentalRequest;
import com.example.videorentalstore.rental.web.dto.BatchRentalResponse;
import com.example.videorentalstore.rental.web.dto.BatchReturnRentalRequest;
import com.example.videorentalstore.rental.web.dto.ReturnRentalRequest;
import com.example.videorentalstore.rental.web.dto.assembler.BatchRentalResponseAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * REST customer's rental resources.
 */
@RestController
@RequestMapping("/customers/{customerId}/rentals")
@Api(tags = "rental")
public class CustomerRentalController {

    private final RentalService rentalService;
    private final BatchRentalResponseAssembler batchRentalResponseAssembler;

    public CustomerRentalController(RentalService rentalService, BatchRentalResponseAssembler batchRentalResponseAssembler) {
        this.rentalService = rentalService;
        this.batchRentalResponseAssembler = batchRentalResponseAssembler;
    }

    @GetMapping
    @ApiOperation(value = "Finds rentals for customer",
            response = BatchRentalResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Customer not found") })
    public ResponseEntity<BatchRentalResponse> getAll(@PathVariable("customerId") Long customerId) {
        final BatchRental batchRental = this.rentalService.findAllForCustomer(customerId);

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }

    @ApiOperation(value = "Creates rentals for customer",
            response = BatchRentalResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid batch create rental request supplied"),
            @ApiResponse(code = 404, message = "Customer not found") })
    @PostMapping
    public ResponseEntity<BatchRentalResponse> create(@PathVariable("customerId") Long customerId, @RequestBody @Valid BatchCreateRentalRequest batchCreateRentalRequest) {
        final BatchRental batchRental = rentalService.create(customerId, batchCreateRentalRequest.toRentalInfoList());

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }

    @ApiOperation(value = "Returns rentals for customer",
            response = BatchRentalResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid batch return rental request supplied"),
            @ApiResponse(code = 404, message = "Customer not found") })
    @PatchMapping
    public ResponseEntity<BatchRentalResponse> returnBack(@PathVariable("customerId") Long customerId, @RequestBody @Valid BatchReturnRentalRequest batchReturnRentalRequest) {
        final BatchRental batchRental = this.rentalService.returnBack(customerId, batchReturnRentalRequest.getReturnRentalRequests().stream().map(ReturnRentalRequest::getRentalId).collect(Collectors.toList()));

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }
}
