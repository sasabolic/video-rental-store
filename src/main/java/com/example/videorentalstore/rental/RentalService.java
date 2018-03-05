package com.example.videorentalstore.rental;

import com.example.videorentalstore.rental.web.CreateRentalRequest;

import java.util.List;

public interface RentalService {

    List<Rental> findAll(Long customerId);

    RentalResponse create(Long customerId, List<CreateRentalRequest> createRentalRequests);

    RentalResponse returnBack(Long customerId, List<Long> rentalIds);
}
