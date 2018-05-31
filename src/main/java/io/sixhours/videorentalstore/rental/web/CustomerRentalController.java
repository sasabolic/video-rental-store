package io.sixhours.videorentalstore.rental.web;

import io.sixhours.videorentalstore.rental.BatchRental;
import io.sixhours.videorentalstore.rental.Rental;
import io.sixhours.videorentalstore.rental.RentalService;
import io.sixhours.videorentalstore.rental.web.dto.*;
import io.sixhours.videorentalstore.rental.web.dto.assembler.BatchRentalResponseAssembler;
import io.sixhours.videorentalstore.rental.web.dto.assembler.RentalResponseAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST customer's rental resources.
 *
 * @author Sasa Bolic
 */
@RestController
@RequestMapping("/customers/{customerId}/rentals")
@Api(tags = "rental")
public class CustomerRentalController {

    private final RentalService rentalService;
    private final RentalResponseAssembler rentalResponseAssembler;
    private final BatchRentalResponseAssembler batchRentalResponseAssembler;

    public CustomerRentalController(RentalService rentalService, RentalResponseAssembler rentalResponseAssembler, BatchRentalResponseAssembler batchRentalResponseAssembler) {
        this.rentalService = rentalService;
        this.rentalResponseAssembler = rentalResponseAssembler;
        this.batchRentalResponseAssembler = batchRentalResponseAssembler;
    }

    @GetMapping
    @ApiOperation(value = "Finds rentals for customer",
            response = BatchRentalResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Customer not found") })
    public ResponseEntity<List<RentalResponse>> getAll(@PathVariable("customerId") Long customerId) {
        final List<Rental> rentals = this.rentalService.findAllForCustomer(customerId);

        return ResponseEntity.ok(rentalResponseAssembler.of(rentals));
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
        final BatchRental batchRental = this.rentalService.returnBack(customerId, batchReturnRentalRequest.getReturnRentalRequests().stream().map(ReturnRentalRequest::getId).collect(Collectors.toList()));

        return ResponseEntity.ok(batchRentalResponseAssembler.of(batchRental));
    }
}
