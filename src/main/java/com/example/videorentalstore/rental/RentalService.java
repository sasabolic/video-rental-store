package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    List<Rental> findAll();

    List<Rental> findAllForCustomer(Long customerId);

    Receipt create(BatchRentalCreateCommand batchRentalCreateCommand);

    Receipt process(BatchRentalCommand batchRentalCommand);
}
