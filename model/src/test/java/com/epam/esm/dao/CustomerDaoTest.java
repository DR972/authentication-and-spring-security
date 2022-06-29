package com.epam.esm.dao;

import com.epam.esm.config.DatabaseTestConfiguration;
import com.epam.esm.entity.Customer;
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

import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_1;
import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_2;
import static com.epam.esm.dao.util.TestDataProvider.CUSTOMER_3;
import static com.epam.esm.dao.util.TestDataProvider.NEW_CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DatabaseTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class CustomerDaoTest {

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
        Optional<Customer> actual = customerDao.findById(3L);
        assertEquals(Optional.of(CUSTOMER_3), actual);
    }

    @Test
    void findListEntitiesShouldReturnResult() {
        List<Customer> actual1 = customerDao.findAll(PageRequest.of(0, 2)).getContent();
        List<Customer> expected1 = Arrays.asList(CUSTOMER_1, CUSTOMER_2);
        assertEquals(expected1, actual1);

        List<Customer> actual2 = customerDao.findAll(PageRequest.of(1, 2)).getContent();
        List<Customer> expected2 = Collections.singletonList(CUSTOMER_3);
        assertEquals(expected2, actual2);
    }

    @Test
    void createEntityShouldReturnResult() {
        Customer actual = customerDao.save(NEW_CUSTOMER);
        assertEquals(NEW_CUSTOMER, actual);
    }

    @Test
    void countNumberEntityRowsShouldReturnResult() {
        long actual = customerDao.count();
        assertEquals(3, actual);
    }
}
