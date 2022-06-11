package com.epam.esm.dao;

import com.epam.esm.config.DatabaseTestConfiguration;
import com.epam.esm.entity.CustomerOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DatabaseTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class CustomerOrderDaoTest {
    private static final GiftCertificate GIFT_CERTIFICATE_1 = new GiftCertificate(1, "ATV riding", "Description ATV riding",
            new BigDecimal("100.00"), 10, LocalDateTime.parse("2022-04-01T10:12:45.123"), LocalDateTime.parse("2022-04-07T14:15:13.257"),
            Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(4, "atv")));

    private static final GiftCertificate GIFT_CERTIFICATE_2 = new GiftCertificate(2, "Horse riding", "Horse riding description",
            new BigDecimal("80.00"), 8, LocalDateTime.parse("2022-04-02T10:12:45.123"), LocalDateTime.parse("2022-04-05T14:15:13.257"),
            Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(5, "horse")));

    private static final GiftCertificate GIFT_CERTIFICATE_3 = new GiftCertificate(3, "Visiting a restaurant", "Visiting the Plaza restaurant",
            new BigDecimal("50.00"), 7, LocalDateTime.parse("2022-04-02T10:12:45.123"), LocalDateTime.parse("2022-04-02T14:15:13.257"),
            Arrays.asList(new Tag(8, "food"), new Tag(10, "restaurant"), new Tag(12, "visit")));

    private static final GiftCertificate GIFT_CERTIFICATE_4 = new GiftCertificate(4, "Visit to the drama theater", "Description visit to the drama theater",
            new BigDecimal("45.00"), 2, LocalDateTime.parse("2022-03-30T10:12:45.123"), LocalDateTime.parse("2022-04-08T14:15:13.257"),
            Arrays.asList(new Tag(6, "theater"), new Tag(12, "visit")));

    private static final GiftCertificate GIFT_CERTIFICATE_5 = new GiftCertificate(5, "Shopping at the tool store", "Description shopping at the tool store",
            new BigDecimal("30.00"), 10, LocalDateTime.parse("2022-03-25T10:12:45.123"), LocalDateTime.parse("2022-04-01T14:15:13.257"),
            Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool")));

    private static final GiftCertificate GIFT_CERTIFICATE_6 = new GiftCertificate(6, "Shopping at the supermarket", "Shopping at Lidl supermarket chain",
            new BigDecimal("80.00"), 12, LocalDateTime.parse("2022-04-01T10:12:45.123"), LocalDateTime.parse("2022-04-14T14:15:13.257"),
            Arrays.asList(new Tag(3, "shopping"), new Tag(8, "food"), new Tag(9, "supermarket")));

    private static final GiftCertificate GIFT_CERTIFICATE_7 = new GiftCertificate(7, "Hot air balloon flight", "An unforgettable hot air balloon flight",
            new BigDecimal("150.00"), 12, LocalDateTime.parse("2022-03-01T10:12:45.123"), LocalDateTime.parse("2022-03-14T14:15:13.257"),
            Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(11, "flight")));

    private static final CustomerOrder CUSTOMER_ORDER_1 = new CustomerOrder(1, 1L, LocalDateTime.parse("2022-04-02T10:12"),
            Collections.singletonList(GIFT_CERTIFICATE_7), new BigDecimal("150.00"));
    private static final CustomerOrder CUSTOMER_ORDER_2 = new CustomerOrder(2, 2L, LocalDateTime.parse("2022-04-03T10:12"),
            Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2), new BigDecimal("180.00"));
    private static final CustomerOrder CUSTOMER_ORDER_3 = new CustomerOrder(3, 3L, LocalDateTime.parse("2022-04-05T10:12"),
            Arrays.asList(GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_4), new BigDecimal("125.00"));
    private static final CustomerOrder CUSTOMER_ORDER_4 = new CustomerOrder(4, 1L, LocalDateTime.parse("2022-04-04T10:12"),
            Arrays.asList(GIFT_CERTIFICATE_5, GIFT_CERTIFICATE_6), new BigDecimal("110.00"));
    private static final CustomerOrder CUSTOMER_ORDER_5 = new CustomerOrder(5, 2L, LocalDateTime.parse("2022-04-07T10:12"),
            Collections.singletonList(GIFT_CERTIFICATE_3), new BigDecimal("50.00"));

    private final DataSource dataSource;
    private final CustomerOrderDao customerOrderDao;

    @Autowired
    public CustomerOrderDaoTest(DataSource dataSource, CustomerOrderDao customerOrderDao) {
        this.dataSource = dataSource;
        this.customerOrderDao = customerOrderDao;
    }

    @BeforeEach
    public void setUp(@Value("classpath:schema.sql") Resource schema) {
        ResourceDatabasePopulator tables = new ResourceDatabasePopulator();
        tables.addScript(schema);
        DatabasePopulatorUtils.execute(tables, dataSource);
    }

    @Test
    void findEntityShouldReturnResult() {
        assertEquals(customerOrderDao.findById(2L), Optional.of(CUSTOMER_ORDER_2));
    }

    @Test
    void findListEntitiesShouldReturnResult() {
        assertEquals(customerOrderDao.findAll(PageRequest.of(0, 3)).getContent(),
                Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_2, CUSTOMER_ORDER_3));

        assertEquals(customerOrderDao.findAll(PageRequest.of(1, 3)).getContent(), Arrays.asList(CUSTOMER_ORDER_4, CUSTOMER_ORDER_5));
    }

    @Test
    void countNumberEntityRowsShouldReturnResult() {
        assertEquals(customerOrderDao.count(), 5);
    }

    @Test
    void findCustomerOrderByIdAndCustomerIdShouldReturnResult() {
        assertEquals(customerOrderDao.findCustomerOrderByIdAndCustomerId(2L,2L), Optional.of(CUSTOMER_ORDER_2));
    }

    @Test
    void findAllByCustomerIdShouldReturnResult() {
        assertEquals(customerOrderDao.findAllByCustomerId(1L,PageRequest.of(0, 3)).getContent(),
                Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_4));

        assertEquals(customerOrderDao.findAllByCustomerId(3L,PageRequest.of(0, 3)).getContent(),
                Collections.singletonList(CUSTOMER_ORDER_3));
    }

    @Test
    void countByCustomerIdShouldReturnResult() {
        assertEquals(customerOrderDao.countByCustomerId(1L), 2);
    }
}
