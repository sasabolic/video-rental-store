package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    List<Rental> findAllForCustomer(Long customerId);

    RentalResponse create(CreateRentalsCommand createRentalsCommand);

    RentalResponse returnBack(ReturnRentalsCommand returnRentalsCommand);
}
