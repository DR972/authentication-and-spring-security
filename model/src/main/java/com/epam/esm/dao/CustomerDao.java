package com.epam.esm.dao;

import com.epam.esm.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface {@code CustomerDao} describes abstract behavior for working in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Repository
public interface CustomerDao extends Dao<Customer, Long> {

    /**
     * The method finds objects Customer in the table 'Customer' by name.
     *
     * @param email String email
     * @return Optional<Customer> object
     */
    Optional<Customer> findCustomerByEmail(String email);
}
