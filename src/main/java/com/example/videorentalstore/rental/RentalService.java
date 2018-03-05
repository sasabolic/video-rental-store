package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    List<Rental> findAll();

    List<Rental> findAllRentedForCustomer(Long customerId);

    List<Rental> findAllForCustomer(Long customerId, Rental.Status status);

    RentalResponse create(CreateRentalsCommand createRentalsCommand);

    RentalResponse returnBack(ReturnRentalsCommand returnRentalsCommand);
}
