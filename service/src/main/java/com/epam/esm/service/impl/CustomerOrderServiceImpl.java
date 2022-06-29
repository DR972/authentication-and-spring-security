package com.epam.esm.service.impl;

import com.epam.esm.dao.Dao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.mapper.EntityMapper;
import com.epam.esm.entity.CustomerOrder;
import com.epam.esm.service.CustomerOrderService;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.service.CustomerService;
import com.epam.esm.service.DateHandler;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class {@code CustomerOrderServiceImpl} is implementation of interface {@link CustomerOrderService}
 * and provides logic to work with {@link com.epam.esm.entity.CustomerOrder}.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Service
public class CustomerOrderServiceImpl extends AbstractService<CustomerOrder, Long, CustomerOrderDto> implements CustomerOrderService {
    /**
     * DateHandler dateHandler.
     */
    private final DateHandler dateHandler;
    /**
     * GiftCertificateService certificateService.
     */
    private final GiftCertificateService certificateService;
    /**
     * CustomerService customerService.
     */
    private final CustomerService customerService;

    /**
     * The constructor creates a TagServiceImpl object
     *
     * @param dao                AbstractDao<T, ID> dao
     * @param entityMapper       EntityMapper<T, D> entityMapper
     * @param dateHandler        DateHandler dateHandler
     * @param certificateService GiftCertificateService certificateService
     * @param customerService    CustomerService customerService
     */
    @Autowired
    public CustomerOrderServiceImpl(Dao<CustomerOrder, Long> dao, EntityMapper<CustomerOrder, CustomerOrderDto> entityMapper,
                                    DateHandler dateHandler, GiftCertificateService certificateService, CustomerService customerService) {
        super(dao, entityMapper);
        this.dateHandler = dateHandler;
        this.certificateService = certificateService;
        this.customerService = customerService;
    }

    @Override
    @Transactional
    public CustomerOrderDto createCustomerOrder(CustomerOrderDto customerOrderDto) {
        System.out.println(customerOrderDto);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(customerService.findCustomerByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId());
        customerOrderDto.setCustomerId(String.valueOf(customerService.findCustomerByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId()));
        customerOrderDto.setPurchaseTime(dateHandler.getCurrentDate());
        List<GiftCertificateDto> certificateDtos = customerOrderDto.getGiftCertificates().stream().map(c -> certificateService.findEntityById(Long.parseLong(c.getCertificateId())))
                .distinct().collect(Collectors.toList());
        BigDecimal amount = certificateDtos.stream().map(c -> new BigDecimal(c.getPrice().replace(',', '.'))).reduce(BigDecimal::add).orElse(new BigDecimal(0));
        customerOrderDto.setGiftCertificates(certificateDtos);
        customerOrderDto.setAmount(amount);
        return entityMapper.convertToDto(dao.save(entityMapper.convertToEntity(customerOrderDto)));
    }
}
