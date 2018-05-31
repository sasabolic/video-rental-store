package io.sixhours.videorentalstore.customer;

import java.util.List;

/**
 * Interface for actions on {@code Customer}.
 *
 * @author Sasa Bolic
 */
public interface CustomerService {

    /**
     * Returns list of {@code Customer} with given {@code name}.
     *
     * @param name the name
     * @return the list
     */
    List<Customer> findAll(String name);

    /**
     * Returns {@code Customer} with given {@code id}.
     *
     * @param id the id
     * @return the customer
     */
    Customer findById(Long id);

    /**
     * Creates new {@code Film} with given values.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @return the customer
     */
    Customer save(String firstName, String lastName);

    /**
     * Updates {@code Customer} with given values.
     *
     * @param id        the id
     * @param firstName the first name
     * @param lastName  the last name
     * @return the customer
     */
    Customer update(Long id, String firstName, String lastName);

    /**
     * Deactivates {@code Customer} with given {@code id}.
     *
     * @param id the id
     * @return the customer
     */
    Customer delete(Long id);
}