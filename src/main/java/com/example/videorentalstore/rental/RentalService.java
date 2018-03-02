package com.example.videorentalstore.rental;

import com.example.videorentalstore.customer.web.RentalItem;

import java.math.BigDecimal;
import java.util.List;

public interface RentalService {

    BigDecimal create(Long customerId, List<RentalItem> rentalItems);
}
