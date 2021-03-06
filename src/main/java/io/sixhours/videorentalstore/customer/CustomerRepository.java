package io.sixhours.videorentalstore.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface to manage {@code Customer} instances.
 *
 * @author Sasa Bolic
 */
@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.active = true")
    @Override
    List<Customer> findAll();

    @Query("SELECT c FROM Customer c WHERE c.id = ?1 AND c.active = true")
    @Override
    Optional<Customer> findById(Long id);

    @Query("SELECT c FROM Customer c WHERE (UPPER(c.firstName) LIKE UPPER(concat('%', ?1,'%')) OR UPPER(c.lastName) LIKE UPPER(concat('%', ?1,'%'))) AND c.active = true")
    List<Customer> findByName(String name);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.active = true")
    @Override
    long count();
}
