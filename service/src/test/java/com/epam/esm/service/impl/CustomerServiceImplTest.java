package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerDao;
import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.CustomerMapper;
import com.epam.esm.dto.mapper.CustomerOrderMapper;
import com.epam.esm.entity.Customer;
import com.epam.esm.entity.CustomerOrder;
import com.epam.esm.entity.Role;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerServiceImpl.class)
public class CustomerServiceImplTest {
    private static final String CUSTOMER_EMAIL_1 = "wse@wss.com";
    private static final String NEW = "new";
    private static final CustomerOrder CUSTOMER_ORDER_1 = new CustomerOrder(1L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("80"));
    private static final CustomerOrder CUSTOMER_ORDER_2 = new CustomerOrder(2L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("180"));
    private static final CustomerOrder CUSTOMER_ORDER_3 = new CustomerOrder(3L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("100"));
    private static final CustomerOrder CUSTOMER_ORDER_5 = new CustomerOrder(5L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("120"));
    private static final CustomerOrder CUSTOMER_ORDER_6 = new CustomerOrder(6L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("60"));
    private static final CustomerOrder CUSTOMER_ORDER_7 = new CustomerOrder(7L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("40"));

    private static final Customer CUSTOMER_1 = new Customer("Customer1", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_5));
    private static final Customer CUSTOMER_2 = new Customer("Customer2", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_2, CUSTOMER_ORDER_6));
    private static final Customer CUSTOMER_3 = new Customer("Customer3", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_3, CUSTOMER_ORDER_7));
    private static final Customer NEW_CUSTOMER = new Customer(0, NEW, new ArrayList<>());

    private static final CustomerOrderDto CUSTOMER_ORDER_DTO_1 = new CustomerOrderDto("1", "1", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("80"));
    private static final CustomerOrderDto CUSTOMER_ORDER_DTO_2 = new CustomerOrderDto("2", "2", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("180"));
    private static final CustomerOrderDto CUSTOMER_ORDER_DTO_3 = new CustomerOrderDto("3", "3", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("100"));
    private static final CustomerOrderDto CUSTOMER_ORDER_DTO_5 = new CustomerOrderDto("5", "1", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("120"));
    private static final CustomerOrderDto CUSTOMER_ORDER_DTO_6 = new CustomerOrderDto("6", "2", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("60"));
    private static final CustomerOrderDto CUSTOMER_ORDER_DTO_7 = new CustomerOrderDto("7", "3", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("40"));

    private static final CustomerDto CUSTOMER_DTO_1 = new CustomerDto("1", "Customer1", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_DTO_1, CUSTOMER_ORDER_DTO_5));
    private static final CustomerDto CUSTOMER_DTO_2 = new CustomerDto("2", "Customer2", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_DTO_2, CUSTOMER_ORDER_DTO_6));
    private static final CustomerDto CUSTOMER_DTO_3 = new CustomerDto("3", "Customer3", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_DTO_3, CUSTOMER_ORDER_DTO_7));
    private static final CustomerDto NEW_DTO_CUSTOMER = new CustomerDto("0", NEW, "qwed", "bvfgtre@wss.com", Role.USER, new ArrayList<>());

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
        assertEquals(CUSTOMER_DTO_2, customerService.findEntityById(2L));
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
        assertEquals(Optional.of(CUSTOMER_1), customerService.findCustomerByEmail(CUSTOMER_EMAIL_1));
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
        assertEquals(new ResourceDto<>(Arrays.asList(CUSTOMER_DTO_1, CUSTOMER_DTO_2, CUSTOMER_DTO_3), 1, 3, 5),
                customerService.findListEntities(1, 5));
    }

    @Test
    void createCustomerShouldReturnResult() {
        when(customerMapper.convertToEntity(NEW_DTO_CUSTOMER)).thenReturn(NEW_CUSTOMER);
        when(customerMapper.convertToDto(NEW_CUSTOMER)).thenReturn(NEW_DTO_CUSTOMER);
        when(customerDao.findCustomerByEmail(NEW)).thenReturn(Optional.empty());
        when(customerDao.save(NEW_CUSTOMER)).thenReturn(NEW_CUSTOMER);
        customerService.createCustomer(NEW_DTO_CUSTOMER);
        verify(customerDao, times(1)).save(NEW_CUSTOMER);
        assertEquals(NEW_DTO_CUSTOMER, customerService.createCustomer(NEW_DTO_CUSTOMER));
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
        assertEquals(CUSTOMER_ORDER_DTO_2, customerService.findCustomerOrderByCustomerIdAndOrderId("2", "2"));
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
        assertEquals(new ResourceDto<>(Arrays.asList(CUSTOMER_ORDER_DTO_3, CUSTOMER_ORDER_DTO_7), 1, 2, 2),
                customerService.findListCustomerOrdersByCustomerId("3", 1, 5));
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
