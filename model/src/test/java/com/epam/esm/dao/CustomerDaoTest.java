package com.epam.esm.dao;

import com.epam.esm.config.DatabaseTestConfiguration;
import com.epam.esm.entity.Customer;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DatabaseTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class CustomerDaoTest {
    private static final String NEW = "new";

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

    private static final CustomerOrder CUSTOMER_ORDER_1 = new CustomerOrder(1, LocalDateTime.parse("2022-04-02T10:12"),
            new ArrayList<>(Collections.singletonList(GIFT_CERTIFICATE_7)), new BigDecimal("150.00"));
    private static final CustomerOrder CUSTOMER_ORDER_2 = new CustomerOrder(2, LocalDateTime.parse("2022-04-03T10:12"),
            new ArrayList<>(Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2)), new BigDecimal("180.00"));
    private static final CustomerOrder CUSTOMER_ORDER_3 = new CustomerOrder(3, LocalDateTime.parse("2022-04-05T10:12"),
            new ArrayList<>(Arrays.asList(GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_4)), new BigDecimal("125.00"));
    private static final CustomerOrder CUSTOMER_ORDER_4 = new CustomerOrder(4, LocalDateTime.parse("2022-04-04T10:12"),
            new ArrayList<>(Arrays.asList(GIFT_CERTIFICATE_5, GIFT_CERTIFICATE_6)), new BigDecimal("110.00"));
    private static final CustomerOrder CUSTOMER_ORDER_5 = new CustomerOrder(5, LocalDateTime.parse("2022-04-07T10:12"),
            new ArrayList<>(Collections.singletonList(GIFT_CERTIFICATE_3)), new BigDecimal("50.00"));

    private static final Customer CUSTOMER_1 = new Customer(1, "user1", Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_4));
    private static final Customer CUSTOMER_2 = new Customer(2, "user2", Arrays.asList(CUSTOMER_ORDER_2, CUSTOMER_ORDER_5));
    private static final Customer CUSTOMER_3 = new Customer(3, "user3", Collections.singletonList(CUSTOMER_ORDER_3));
    private static final Customer NEW_CUSTOMER = new Customer(0, NEW, new ArrayList<>());

    private final DataSource dataSource;
    private final CustomerDao customerDao;

    @Autowired
    public CustomerDaoTest(DataSource dataSource, CustomerDao customerDao) {
        this.dataSource = dataSource;
        this.customerDao = customerDao;
    }

    @BeforeEach
    public void setUp(@Value("classpath:schema.sql") Resource schema) {
        ResourceDatabasePopulator tables = new ResourceDatabasePopulator();
        tables.addScript(schema);
        DatabasePopulatorUtils.execute(tables, dataSource);
    }

    @Test
    void findEntityShouldReturnResult() {
        assertEquals(customerDao.findById(3L), Optional.of(CUSTOMER_3));
    }

    @Test
    void findListEntitiesShouldReturnResult() {
        assertEquals(customerDao.findAll(PageRequest.of(0, 2)).getContent(), Arrays.asList(CUSTOMER_1, CUSTOMER_2));
        assertEquals(customerDao.findAll(PageRequest.of(1, 2)).getContent(), Collections.singletonList(CUSTOMER_3));
    }

    @Test
    void createEntityShouldReturnResult() {
        assertEquals(customerDao.save(NEW_CUSTOMER), NEW_CUSTOMER);
    }

    @Test
    void countNumberEntityRowsShouldReturnResult() {
        assertEquals(customerDao.count(), 3);
    }
}
