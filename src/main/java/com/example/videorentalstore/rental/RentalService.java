package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    List<Rental> findAll();

    List<Rental> findAllForCustomer(Long customerId);

    Receipt create(CreateRentalsCommand createRentalsCommand);

    Receipt returnBack(ReturnRentalsCommand returnRentalsCommand);
}
