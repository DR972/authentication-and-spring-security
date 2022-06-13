package com.epam.esm.dao;

import com.epam.esm.config.DatabaseTestConfiguration;
import com.epam.esm.entity.CustomerOrder;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_ORDER_1;
import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_ORDER_2;
import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_ORDER_3;
import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_ORDER_4;
import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_ORDER_5;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DatabaseTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class CustomerOrderDaoTest {

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
        Optional<CustomerOrder> actual = customerOrderDao.findById(2L);
        assertEquals(Optional.of(CUSTOMER_ORDER_2), actual);
    }

    @Test
    void findListEntitiesShouldReturnResult() {
        List<CustomerOrder> actual1 = customerOrderDao.findAll(PageRequest.of(0, 3)).getContent();
        List<CustomerOrder> expected1 = Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_2, CUSTOMER_ORDER_3);
        assertEquals(expected1, actual1);

        List<CustomerOrder> actual2 = customerOrderDao.findAll(PageRequest.of(1, 3)).getContent();
        List<CustomerOrder> expected2 = Arrays.asList(CUSTOMER_ORDER_4, CUSTOMER_ORDER_5);
        assertEquals(expected2, actual2);
    }

    @Test
    void countNumberEntityRowsShouldReturnResult() {
        long actual = customerOrderDao.count();
        assertEquals(5, actual);
    }

    @Test
    void findCustomerOrderByIdAndCustomerIdShouldReturnResult() {
        Optional<CustomerOrder> actual = customerOrderDao.findCustomerOrderByIdAndCustomerId(2L, 2L);
        assertEquals(Optional.of(CUSTOMER_ORDER_2), actual);
    }

    @Test
    void findAllByCustomerIdShouldReturnResult() {
        List<CustomerOrder> actual1 = customerOrderDao.findAllByCustomerId(1L, PageRequest.of(0, 3)).getContent();
        List<CustomerOrder> expected1 = Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_4);
        assertEquals(expected1, actual1);

        List<CustomerOrder> actual2 = customerOrderDao.findAllByCustomerId(3L, PageRequest.of(0, 3)).getContent();
        List<CustomerOrder> expected2 = Collections.singletonList(CUSTOMER_ORDER_3);
        assertEquals(expected2, actual2);
    }

    @Test
    void countByCustomerIdShouldReturnResult() {
        long actual = customerOrderDao.countByCustomerId(1L);
        assertEquals(2, actual);
    }
}
