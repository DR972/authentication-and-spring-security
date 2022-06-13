package com.epam.esm.dao;

import com.epam.esm.config.DatabaseTestConfiguration;
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

import static com.epam.esm.dao.util.TestDataProvider.NEW_TAG;
import static com.epam.esm.dao.util.TestDataProvider.NEW_TAG_5;
import static com.epam.esm.dao.util.TestDataProvider.TAG_1;
import static com.epam.esm.dao.util.TestDataProvider.TAG_10;
import static com.epam.esm.dao.util.TestDataProvider.TAG_11;
import static com.epam.esm.dao.util.TestDataProvider.TAG_12;
import static com.epam.esm.dao.util.TestDataProvider.TAG_2;
import static com.epam.esm.dao.util.TestDataProvider.TAG_3;
import static com.epam.esm.dao.util.TestDataProvider.TAG_4;
import static com.epam.esm.dao.util.TestDataProvider.TAG_5;
import static com.epam.esm.dao.util.TestDataProvider.TAG_6;
import static com.epam.esm.dao.util.TestDataProvider.TAG_7;
import static com.epam.esm.dao.util.TestDataProvider.TAG_8;
import static com.epam.esm.dao.util.TestDataProvider.TAG_9;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DatabaseTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class TagDaoTest {
    private final DataSource dataSource;
    private final TagDao tagDao;

    @Autowired
    public TagDaoTest(DataSource dataSource, TagDao tagDao) {
        this.dataSource = dataSource;
        this.tagDao = tagDao;
    }

    @BeforeEach
    public void setUp(@Value("classpath:schema.sql") Resource schema) {
        ResourceDatabasePopulator tables = new ResourceDatabasePopulator();
        tables.addScript(schema);
        DatabasePopulatorUtils.execute(tables, dataSource);
    }

    @Test
    void findEntityByIdShouldReturnResult() {
        Optional<Tag> actual = tagDao.findById(1L);
        assertEquals(Optional.of(TAG_1), actual);
    }

    @Test
    void findTagByNameShouldReturnResult() {
        Optional<Tag> actual = tagDao.findTagByName(TAG_2.getName());
        assertEquals(Optional.of(TAG_2), actual);
    }

    @Test
    void findListEntitiesShouldReturnResult() {
        List<Tag> actual1 = tagDao.findAll(PageRequest.of(0, 10)).getContent();
        List<Tag> expected1 = Arrays.asList(TAG_1, TAG_2, TAG_3, TAG_4, TAG_5, TAG_6, TAG_7, TAG_8, TAG_9, TAG_10);
        assertEquals(expected1, actual1);

        List<Tag> actual2 = tagDao.findAll(PageRequest.of(1, 10)).getContent();
        List<Tag> expected2 = Arrays.asList(TAG_11, TAG_12);
        assertEquals(expected2, actual2);
    }

    @Test
    void createEntityShouldReturnResult() {
        Tag actual = tagDao.save(NEW_TAG);
        assertEquals(NEW_TAG, actual);
    }

    @Test
    void updateEntityShouldReturnResult() {
        Tag actual = tagDao.save(NEW_TAG_5);
        assertEquals(NEW_TAG_5, actual);
    }

    @Test
    void findMostWidelyUsedTagsOfCustomersWithHighestCostOfAllOrdersShouldReturnResult() {
        List<Tag> actual = tagDao.findMostPopularTag(PageRequest.of(0, 5)).getContent();
        List<Tag> expected = Collections.singletonList(TAG_3);
        assertEquals(expected, actual);
    }

    @Test
    void countNumberEntityRowsShouldReturnResult() {
        long actual = tagDao.count();
        assertEquals(12, actual);
    }
}
