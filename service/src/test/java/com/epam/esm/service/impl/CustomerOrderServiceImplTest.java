package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.CustomerOrderMapper;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.CustomerService;
import com.epam.esm.service.DateHandler;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;

import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_DTO_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_1;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_3;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_DTO_1;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_DTO_2;
import static com.epam.esm.service.util.TestDataProvider.CUSTOMER_ORDER_DTO_3;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_1;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_2;
import static com.epam.esm.service.util.TestDataProvider.NEW_CUSTOMER_ORDER;
import static com.epam.esm.service.util.TestDataProvider.NEW_DTO_CUSTOMER_ORDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomerOrderServiceImpl.class)
public class CustomerOrderServiceImplTest {
    @MockBean
    private CustomerOrderDao customerOrderDao;
    @MockBean
    private CustomerOrderMapper customerOrderMapper;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private GiftCertificateService certificateService;
    @MockBean
    private DateHandler dateHandler;
    @MockBean
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;
    @Autowired
    private CustomerOrderServiceImpl customerOrderService;

    @Test
    void findCustomerOrderByIdShouldReturnResult() {
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_2)).thenReturn(CUSTOMER_ORDER_DTO_2);
        when(customerOrderDao.findById(2L)).thenReturn(Optional.of(CUSTOMER_ORDER_2));

        customerOrderService.findEntityById(2L);
        verify(customerOrderDao, times(1)).findById(2L);

        CustomerOrderDto actual = customerOrderService.findEntityById(2L);
        assertEquals(CUSTOMER_ORDER_DTO_2, actual);
    }

    @Test
    void findCustomerOrderByIddShouldThrowException() {
        when(customerOrderDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> customerOrderService.findEntityById(2L));
        verify(customerOrderDao, times(1)).findById(2L);

        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findAllCustomerOrdersShouldReturnResult() {
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_1)).thenReturn(CUSTOMER_ORDER_DTO_1);
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_2)).thenReturn(CUSTOMER_ORDER_DTO_2);
        when(customerOrderMapper.convertToDto(CUSTOMER_ORDER_3)).thenReturn(CUSTOMER_ORDER_DTO_3);
        when(customerOrderDao.count()).thenReturn(5L);
        when(customerOrderDao.findAll(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_2, CUSTOMER_ORDER_3)));

        customerOrderService.findListEntities(1, 5);
        verify(customerOrderDao, times(1)).findAll(PageRequest.of(0, 5));

        ResourceDto<CustomerOrderDto> actual = customerOrderService.findListEntities(1, 5);
        ResourceDto<CustomerOrderDto> expected = new ResourceDto<>(Arrays.asList(CUSTOMER_ORDER_DTO_1, CUSTOMER_ORDER_DTO_2, CUSTOMER_ORDER_DTO_3), 1, 3, 5);
        assertEquals(expected, actual);
    }

    @Test
    void createCustomerOrderShouldReturnResult() {
        when(customerService.findEntityById(2L)).thenReturn(CUSTOMER_DTO_2);
        when(certificateService.findEntityById(1L)).thenReturn(GIFT_CERTIFICATE_DTO_1);
        when(certificateService.findEntityById(2L)).thenReturn(GIFT_CERTIFICATE_DTO_2);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getName()).thenReturn(CUSTOMER_2.getEmail());
        when(customerService.findCustomerByEmail(CUSTOMER_2.getEmail())).thenReturn(Optional.of(CUSTOMER_2));

        when(customerOrderMapper.convertToEntity(NEW_DTO_CUSTOMER_ORDER)).thenReturn(NEW_CUSTOMER_ORDER);
        when(customerOrderMapper.convertToDto(NEW_CUSTOMER_ORDER)).thenReturn(NEW_DTO_CUSTOMER_ORDER);
        when(customerOrderDao.save(NEW_CUSTOMER_ORDER)).thenReturn(NEW_CUSTOMER_ORDER);

        customerOrderService.createCustomerOrder(NEW_DTO_CUSTOMER_ORDER);
        verify(customerOrderDao, times(1)).save(NEW_CUSTOMER_ORDER);

        CustomerOrderDto actual = customerOrderService.createCustomerOrder(NEW_DTO_CUSTOMER_ORDER);
        assertEquals(NEW_DTO_CUSTOMER_ORDER, actual);
    }
}
