package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerDao;
import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.CustomerMapper;
import com.epam.esm.dto.mapper.CustomerOrderMapper;
import com.epam.esm.entity.Customer;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_1;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_3;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_DTO_1;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_DTO_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_DTO_3;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_EMAIL_1;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_3;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_7;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_DTO_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_DTO_3;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_DTO_7;
import static com.epam.esm.service.util.TestDataProvider.NEW;
import static com.epam.esm.service.util.TestDataProvider.NEW_CUSTOMER;
import static com.epam.esm.service.util.TestDataProvider.NEW_DTO_CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerServiceImpl.class)
public class CustomerServiceImplTest {
    @MockBean
    private CustomerDao customerDao;
    @MockBean
    private CustomerMapper customerMapper;
    @MockBean
    private CustomerOrderDao customerOrderDao;
    @MockBean
    private CustomerOrderMapper customerOrderMapper;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncode;
    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void findCustomerByIdShouldReturnResult() {
        when(customerMapper.convertToDto(CUSTOMER_2)).thenReturn(CUSTOMER_DTO_2);
        when(customerDao.findById(2L)).thenReturn(Optional.of(CUSTOMER_2));

        customerService.findEntityById(2L);
        verify(customerDao, times(1)).findById(2L);

        CustomerDto actual = customerService.findEntityById(2L);
        assertEquals(CUSTOMER_DTO_2, actual);
    }

    @Test
    void findCustomerByIdShouldThrowException() {
        when(customerDao.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchEntityException.class, () -> customerService.findEntityById(2L));
        verify(customerDao, times(1)).findById(2L);

        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findCustomerByNameShouldReturnResult() {
        when(customerDao.findCustomerByEmail(CUSTOMER_EMAIL_1)).thenReturn(Optional.of(CUSTOMER_1));

        customerService.findCustomerByEmail(CUSTOMER_EMAIL_1);
        verify(customerDao, times(1)).findCustomerByEmail(CUSTOMER_EMAIL_1);

        Optional<Customer> actual = customerService.findCustomerByEmail(CUSTOMER_EMAIL_1);
        assertEquals(Optional.of(CUSTOMER_1), actual);
    }

    @Test
    void findListCustomersShouldReturnResult() {
        when(customerMapper.convertToDto(CUSTOMER_1)).thenReturn(CUSTOMER_DTO_1);
        when(customerMapper.convertToDto(CUSTOMER_2)).thenReturn(CUSTOMER_DTO_2);
        when(customerMapper.convertToDto(CUSTOMER_3)).thenReturn(CUSTOMER_DTO_3);
        when(customerDao.count()).thenReturn(5L);
        when(customerDao.findAll(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(Arrays.asList(CUSTOMER_1, CUSTOMER_2, CUSTOMER_3)));

        customerService.findListEntities(1, 5);
        verify(customerDao, times(1)).findAll(PageRequest.of(0, 5));

        ResourceDto<CustomerDto> actual =  customerService.findListEntities(1, 5);
        ResourceDto<CustomerDto> expected = new ResourceDto<>(Arrays.asList(CUSTOMER_DTO_1, CUSTOMER_DTO_2, CUSTOMER_DTO_3), 1, 3, 5);
        assertEquals(expected, actual);
    }

    @Test
    void createCustomerShouldReturnResult() {
        when(customerMapper.convertToEntity(NEW_DTO_CUSTOMER)).thenReturn(NEW_CUSTOMER);
        when(customerMapper.convertToDto(NEW_CUSTOMER)).thenReturn(NEW_DTO_CUSTOMER);
        when(customerDao.findCustomerByEmail(NEW)).thenReturn(Optional.empty());
        when(customerDao.save(NEW_CUSTOMER)).thenReturn(NEW_CUSTOMER);

        customerService.createCustomer(NEW_DTO_CUSTOMER);
        verify(customerDao, times(1)).save(NEW_CUSTOMER);

        CustomerDto actual = customerService.createCustomer(NEW_DTO_CUSTOMER);
        assertEquals(NEW_DTO_CUSTOMER, actual);
    }

    @Test
    void createCustomerShouldThrowException() {
        when(customerMapper.convertToEntity(CUSTOMER_DTO_1)).thenReturn(CUSTOMER_1);
        when(customerDao.findCustomerByEmail(CUSTOMER_EMAIL_1)).thenReturn(Optional.of(CUSTOMER_1));

        Exception exception = assertThrows(DuplicateEntityException.class, () -> customerService.createCustomer(CUSTOMER_DTO_1));
        verify(customerDao, times(1)).findCustomerByEmail(CUSTOMER_EMAIL_1);

        assertTrue(exception.getMessage().contains("ex.duplicate"));
    }

    @Test
    void findCustomerOrderByCustomerIdAndOrderIdShouldReturnResult() {
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_2)).thenReturn(CUSTOMER_ORDER_DTO_2);
        when(customerOrderDao.findCustomerOrderByIdAndCustomerId(2, 2)).thenReturn(Optional.of(CUSTOMER_ORDER_2));

        customerService.findCustomerOrderByCustomerIdAndOrderId("2", "2");
        verify(customerOrderDao, times(1)).findCustomerOrderByIdAndCustomerId(2, 2);

        CustomerOrderDto actual = customerService.findCustomerOrderByCustomerIdAndOrderId("2", "2");
        assertEquals(CUSTOMER_ORDER_DTO_2, actual);
    }

    @Test
    void findCustomerOrderByCustomerIdAndOrderIdShouldThrowException() {
        when(customerOrderDao.findCustomerOrderByIdAndCustomerId(2, 2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchEntityException.class, () -> customerService.findCustomerOrderByCustomerIdAndOrderId("2", "2"));
        verify(customerOrderDao, times(1)).findCustomerOrderByIdAndCustomerId(2, 2);

        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findAllCustomerOrdersByCustomerIdShouldReturnResult() {
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_3)).thenReturn(CUSTOMER_ORDER_DTO_3);
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_7)).thenReturn(CUSTOMER_ORDER_DTO_7);
        when(customerMapper.convertToDto(CUSTOMER_3)).thenReturn(CUSTOMER_DTO_3);
        when(customerOrderDao.countByCustomerId(3)).thenReturn(2L);
        when(customerDao.findById(3L)).thenReturn(Optional.of(CUSTOMER_3));
        when(customerOrderDao.findAllByCustomerId(3, PageRequest.of(0, 5))).thenReturn(new PageImpl<>(Arrays.asList(CUSTOMER_ORDER_3, CUSTOMER_ORDER_7)));

        customerService.findListCustomerOrdersByCustomerId("3", 1, 5);
        verify(customerOrderDao, times(1)).findAllByCustomerId(3, PageRequest.of(0, 5));

        ResourceDto<CustomerOrderDto> actual = customerService.findListCustomerOrdersByCustomerId("3", 1, 5);
        ResourceDto<CustomerOrderDto> expected = new ResourceDto<>(Arrays.asList(CUSTOMER_ORDER_DTO_3, CUSTOMER_ORDER_DTO_7), 1, 2, 2);
        assertEquals(expected, actual);
    }

    @Test
    void findAllCustomerOrdersByCustomerIdShouldThrowException() {
        when(customerMapper.convertToEntity(CUSTOMER_DTO_1)).thenReturn(CUSTOMER_1);
        when(customerDao.findById(9L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchEntityException.class, () -> customerService.findListCustomerOrdersByCustomerId("9", 1, 5));
        verify(customerDao, times(1)).findById(9L);

        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }
}
