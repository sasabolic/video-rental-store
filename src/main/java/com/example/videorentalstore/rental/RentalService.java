package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    List<Rental> findAll();

    List<Rental> findAllForCustomer(Long customerId, Rental.Status status);

    Receipt create(BatchRentalCreateCommand batchRentalCreateCommand);

    List<Rental> create(Long customerId, List<RentalInfo> rentalInfos);

    List<Rental> returnBack(Long customerId, List<Long> rentalIds);

    void delete(Long customerId, List<Long> ids);
}
