package com.epam.esm.service;

import com.epam.esm.dto.CustomerOrderDto;

/**
 * The interface {@code CustomerOrderService} describes abstract behavior for working with
 * {@link com.epam.esm.service.impl.CustomerOrderServiceImpl} objects.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public interface CustomerOrderService extends BaseService<Long, CustomerOrderDto> {

    /**
     * The method performs the operation of saving CustomerOrder.
     *
     * @param customerOrderDto CustomerOrderDto
     * @return CustomerOrderDto object
     */
    CustomerOrderDto createCustomerOrder(CustomerOrderDto customerOrderDto);
}
