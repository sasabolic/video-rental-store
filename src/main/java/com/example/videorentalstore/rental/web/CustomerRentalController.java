package com.example.videorentalstore.rental.web;

import com.example.videorentalstore.rental.*;
import com.example.videorentalstore.rental.web.dto.ReceiptDto;
import com.example.videorentalstore.rental.web.dto.RentalDto;
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
    public ResponseEntity<List<RentalDto>> getAll(@PathVariable("id") long customerId) {
        final List<Rental> rentals = this.rentalService.findAllRentedForCustomer(customerId);

        return ResponseEntity.ok(convertToRentalDtoList(rentals));
    }

    @PostMapping("/customers/{id}/rentals")
    public ResponseEntity<ReceiptDto> create(@PathVariable("id") long customerId, @RequestBody List<CreateRentalRequest> createRentalRequests) {
        CreateRentalsCommand createRentalsCommand = new CreateRentalsCommand(customerId, createRentalRequests.stream().map(r -> r.toCreateRentalCommand()).collect(Collectors.toList()));

        final Receipt receipt = rentalService.create(createRentalsCommand);

        return ResponseEntity.ok(covertToReceiptDto(receipt));
    }

    @PatchMapping("/customers/{id}/rentals")
    public ResponseEntity<ReceiptDto> update(@PathVariable("id") long customerId, @RequestBody List<Long> rentalIds) {
        ReturnRentalsCommand returnRentalsCommand = new ReturnRentalsCommand(customerId, rentalIds);

        final Receipt receipt = this.rentalService.returnBack(returnRentalsCommand);

        return ResponseEntity.ok(covertToReceiptDto(receipt));
    }

    private ReceiptDto covertToReceiptDto(Receipt receipt) {
        final List<RentalDto> rentalDtos = convertToRentalDtoList(receipt.getRentals());

        return new ReceiptDto(receipt.getAmount(), rentalDtos);
    }

    private List<RentalDto> convertToRentalDtoList(List<Rental> rentals) {
        return rentals.stream()
                .map(r -> new RentalDto(r.getId(), r.getFilm().getName(), r.getDaysRented(), r.getStartDate(), r.getEndDate(), r.getStatus().name())).collect(Collectors.toList());
    }
}
