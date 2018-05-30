package com.example.videorentalstore.rental;

import java.util.List;

/**
 * Interface for actions on rentals.
 *
 * @author Sasa Bolic
 */
public interface RentalService {

    /**
     * Returns all rentals for customer with given {@code customerId}.
     *
     *
     * @param customerId the customer id
     * @return the list
     */
    List<Rental> findAllForCustomer(Long customerId);

    /**
     * Creates new rental from {@code rentalInfos} for customer with given {@code customer id}.
     *
     * @param customerId  the customer id
     * @param rentalInfos the rental infos
     * @return the batch rental which contains list of rentals and total price of renting
     */
    BatchRental create(Long customerId, List<RentalInfo> rentalInfos);

    /**
     * Returns back batch of rentals defined by list of {@code rentalIds} for customer with {@code customerId}.
     *
     * @param customerId the customer id
     * @param rentalIds  the rental ids
     * @return the batch rental which contains list of rentals and total price of renting
     */
    BatchRental returnBack(Long customerId, List<Long> rentalIds);
}
