package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    List<Rental> findAll();

    List<Rental> findAllForCustomer(Long customerId, Rental.Status status);

    RentalResult create(Long customerId, List<RentalInfo> rentalInfos);

    RentalResult returnBack(Long customerId, List<Long> rentalIds);

    void delete(Long customerId, List<Long> ids);
}
