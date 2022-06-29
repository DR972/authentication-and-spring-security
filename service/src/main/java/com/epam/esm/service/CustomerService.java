package com.epam.esm.service;

import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.entity.Customer;

import java.util.Optional;

/**
 * The interface {@code CustomerService} describes abstract behavior for working with {@link com.epam.esm.service.impl.CustomerServiceImpl} objects.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public interface CustomerService extends BaseService<Customer, Long, CustomerDto> {

    /**
     * The method finds Customer by name.
     *
     * @param email Customer email
     * @return Optional<Customer> object
     */
    Optional<Customer> findCustomerByEmail(String email);

    /**
     * The method performs the operation of saving Customer.
     *
     * @param customerDto CustomerDto
     * @return CustomerDto object
     */
    CustomerDto createCustomer(CustomerDto customerDto);

    /**
     * The method finds Customer by Customer id and Order id.
     *
     * @param customerId Customer customerId
     * @param orderId    Customer orderId
     * @return CustomerDto object
     */
    CustomerOrderDto findCustomerOrderByCustomerIdAndOrderId(String customerId, String orderId);

    /**
     * The method finds list Customer orders by Customer id.
     *
     * @param customerId Customer customerId
     * @param pageNumber int pageNumber
     * @param limit       int limit
     * @return ResourceDto<CustomerOrderDto>
     */
    ResourceDto<CustomerOrderDto> findListCustomerOrdersByCustomerId(String customerId, int pageNumber, int limit);
}
