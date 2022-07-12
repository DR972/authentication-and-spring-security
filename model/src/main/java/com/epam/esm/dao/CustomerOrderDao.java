package com.epam.esm.dao;

import com.epam.esm.entity.CustomerOrder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface {@code CustomerOrderDao} describes abstract behavior for working in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Repository
public interface CustomerOrderDao extends Dao<CustomerOrder, Long> {

    /**
     * The method finds objects CustomerOrder in the table 'CustomerOrder' by CustomerOrder i and Customer i.
     *
     * @param customerId long customerId
     * @param orderId    long orderId
     * @return Optional<CustomerOrder> object
     */
    Optional<CustomerOrder> findCustomerOrderByIdAndCustomerId(long orderId, long customerId);

    /**
     * The method finds objects CustomerOrder in the table 'CustomerOrder' by GiftCertificate.
     *
     * @param certificate GiftCertificate certificate
     * @return Optional<CustomerOrder> object
     */
    List<CustomerOrder> findCustomerOrderByGiftCertificates(GiftCertificate certificate);

    /**
     * The method finds list CustomerOrder objects in the table `CustomerOrder` by Customer i.
     *
     * @param customerId long customerId
     * @param pageable   Pageable pageable
     * @return Page<CustomerOrder> objects
     */
    Page<CustomerOrder> findAllByCustomerId(long customerId, Pageable pageable);

    /**
     * The method finds count number of rows Customer Orders objects by Customer i.
     *
     * @param customerId long customerId
     * @return count number of rows objects
     */
    long countByCustomerId(long customerId);
}
