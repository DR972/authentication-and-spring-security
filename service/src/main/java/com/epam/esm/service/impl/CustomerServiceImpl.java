package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerDao;
import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dao.Dao;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.CustomerOrderMapper;
import com.epam.esm.dto.mapper.EntityMapper;
import com.epam.esm.entity.Customer;
import com.epam.esm.entity.Role;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.CustomerService;
import com.epam.esm.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class {@code CustomerServiceImpl} is implementation of interface {@link CustomerService}
 * and provides logic to work with {@link com.epam.esm.entity.Customer}.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Service
public class CustomerServiceImpl extends AbstractService<Customer, Long, CustomerDto> implements CustomerService {
    /**
     * CustomerOrderMapper customerOrderMapper.
     */
    private final CustomerOrderMapper customerOrderMapper;
    /**
     * CustomerOrderDao customerOrderDao.
     */
    private final CustomerOrderDao customerOrderDao;
    /**
     * CustomerOrderDao customerOrderDao.
     */
    private final CustomerDao customerDao;
    /**
     * BCryptPasswordEncoder bCryptPasswordEncode.
     */
    private final BCryptPasswordEncoder bCryptPasswordEncode;

    /**
     * The constructor creates a CustomerServiceImpl object
     *
     * @param dao                  AbstractDao<Tag, Long> dao
     * @param entityMapper         EntityMapper<Tag, TagDto> entityMapper
     * @param customerOrderMapper  CustomerOrderMapper customerOrderMapper
     * @param customerOrderDao     CustomerOrderDao customerOrderDao
     * @param customerDao          CustomerDao CustomerDao
     * @param bCryptPasswordEncode BCryptPasswordEncoder bCryptPasswordEncode
     */
    @Autowired
    public CustomerServiceImpl(Dao<Customer, Long> dao, EntityMapper<Customer, CustomerDto> entityMapper, CustomerOrderMapper customerOrderMapper,
                               CustomerOrderDao customerOrderDao, CustomerDao customerDao, BCryptPasswordEncoder bCryptPasswordEncode) {
        super(dao, entityMapper);
        this.customerOrderMapper = customerOrderMapper;
        this.customerOrderDao = customerOrderDao;
        this.customerDao = customerDao;
        this.bCryptPasswordEncode = bCryptPasswordEncode;
    }

    @Override
    public Optional<Customer> findCustomerByEmail(String email) {
        return customerDao.findCustomerByEmail(email);
    }

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = entityMapper.convertToEntity(customerDto);
        if (findCustomerByEmail(customer.getEmail()).isPresent()) {
            throw new DuplicateEntityException("ex.duplicate", customer.getEmail());
        }
        customer.setPassword(bCryptPasswordEncode.encode(customer.getPassword()));
        customer.setRole(Role.USER);
        return entityMapper.convertToDto(dao.save(customer));
    }

    @Override
    public CustomerOrderDto findCustomerOrderByCustomerIdAndOrderId(String customerId, String orderId) {
        return customerOrderMapper.convertToDto(customerOrderDao.findCustomerOrderByIdAndCustomerId(Long.parseLong(orderId), Long.parseLong(customerId)).orElseThrow(() ->
                new NoSuchEntityException("ex.noSuchEntity", "customerId = " + customerId + ", orderId = " + orderId)));
    }

    @Override
    @Transactional
    public ResourceDto<CustomerOrderDto> findListCustomerOrdersByCustomerId(String customerId, int pageNumber, int limit) {
        findEntityById(Long.parseLong(customerId));
        List<CustomerOrderDto> customerOrders = customerOrderDao.findAllByCustomerId(Long.parseLong(customerId), PageRequest.of(pageNumber - 1, limit))
                .stream().map(customerOrderMapper::convertToDto).collect(Collectors.toList());
        return new ResourceDto<>(customerOrders, pageNumber, customerOrders.size(), customerOrderDao
                .countByCustomerId(Long.parseLong(customerId)));
    }
}
