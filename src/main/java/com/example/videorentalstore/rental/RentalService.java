package com.example.videorentalstore.rental;

import java.util.List;

public interface RentalService {

    BatchRental findAllForCustomer(Long customerId);

    BatchRental create(Long customerId, List<RentalInfo> rentalInfos);

    BatchRental returnBack(Long customerId, List<Long> rentalIds);
}
