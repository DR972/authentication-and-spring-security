package com.epam.esm.dao;

import com.epam.esm.config.DatabaseTestConfiguration;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import static com.epam.esm.dao.util.TestDataProvider.GIFT_CERTIFICATE_1;
import static com.epam.esm.dao.util.TestDataProvider.GIFT_CERTIFICATE_2;
import static com.epam.esm.dao.util.TestDataProvider.GIFT_CERTIFICATE_3;
import static com.epam.esm.dao.util.TestDataProvider.GIFT_CERTIFICATE_4;
import static com.epam.esm.dao.util.TestDataProvider.GIFT_CERTIFICATE_5;
import static com.epam.esm.dao.util.TestDataProvider.GIFT_CERTIFICATE_7;
import static com.epam.esm.dao.util.TestDataProvider.HORSE;
import static com.epam.esm.dao.util.TestDataProvider.LAST_UPDATE_DATE;
import static com.epam.esm.dao.util.TestDataProvider.NAME;
import static com.epam.esm.dao.util.TestDataProvider.NEW_GIFT_CERTIFICATE;
import static com.epam.esm.dao.util.TestDataProvider.NEW_GIFT_CERTIFICATE_WITH_NEW_TAG;
import static com.epam.esm.dao.util.TestDataProvider.REST;
import static com.epam.esm.dao.util.TestDataProvider.RIDING;
import static com.epam.esm.dao.util.TestDataProvider.SCRIPT;
import static com.epam.esm.dao.util.TestDataProvider.UPDATE_GIFT_CERTIFICATE_1;
import static com.epam.esm.dao.util.TestDataProvider.UPDATE_GIFT_CERTIFICATE_2;
import static com.epam.esm.dao.util.TestDataProvider.VISIT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DatabaseTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
public class GiftCertificateDaoTest {

    private final DataSource dataSource;
    private final GiftCertificateDao certificateDao;

    @Autowired
    public GiftCertificateDaoTest(DataSource dataSource, GiftCertificateDao certificateDao) {
        this.dataSource = dataSource;
        this.certificateDao = certificateDao;
    }

    @BeforeEach
    public void setUp(@Value("classpath:schema.sql") Resource schema) {
        ResourceDatabasePopulator tables = new ResourceDatabasePopulator();
        tables.addScript(schema);
        DatabasePopulatorUtils.execute(tables, dataSource);
    }

    @Test
    void findEntity() {
        Optional<GiftCertificate> actual = certificateDao.findById(2L);
        assertEquals(Optional.of(GIFT_CERTIFICATE_2), actual);
    }

    @Test
    void findAllByNameAndDescriptionShouldReturnResult() {
        List<GiftCertificate> actual1 = certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", "", "", PageRequest.of(0, 5, Sort.by(LAST_UPDATE_DATE))).getContent();
        List<GiftCertificate> expected1 = Arrays.asList(GIFT_CERTIFICATE_7, GIFT_CERTIFICATE_5, GIFT_CERTIFICATE_3, GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_1);
        assertEquals(expected1, actual1);

        List<GiftCertificate> actual2 = certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                (VISIT, "", VISIT, "", PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, LAST_UPDATE_DATE))).getContent();
        List<GiftCertificate> expected2 = Arrays.asList(GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_3);
        assertEquals(expected2, actual2);
    }

    @Test
    void countByNameAndDescriptionShouldReturnResult() {
        long actual1 = certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", "", "");
        assertEquals(7, actual1);

        long actual2 = certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                (VISIT, "", VISIT, "");
        assertEquals(2, actual2);
    }

    @Test
    void findAllByNameAndDescriptionAndTagShouldReturnResult() {
        List<GiftCertificate> actual1 = certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", "", "", REST, PageRequest.of(0, 5, Sort.by(NAME))).getContent();
        List<GiftCertificate> expected1 = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_7);
        assertEquals(expected1, actual1);

        List<GiftCertificate> actual2 = certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                (RIDING, "", SCRIPT, "", HORSE, PageRequest.of(0, 5, Sort.by(LAST_UPDATE_DATE))).getContent();
        List<GiftCertificate> expected2 = Collections.singletonList(GIFT_CERTIFICATE_2);
        assertEquals(expected2, actual2);
    }

    @Test
    void countByNameAndDescriptionAndTagShouldReturnResult() {
        long actual1 = certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", "", "", REST);
        assertEquals(3, actual1);

        long actual2 = certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                (RIDING, "", SCRIPT, "", HORSE);
        assertEquals(1, actual2);
    }

    @Test
    void createEntityShouldReturnResult() {
        GiftCertificate actual1 = certificateDao.save(NEW_GIFT_CERTIFICATE);
        assertEquals(NEW_GIFT_CERTIFICATE, actual1);

        GiftCertificate actual2 = certificateDao.save(NEW_GIFT_CERTIFICATE_WITH_NEW_TAG);
        assertEquals(NEW_GIFT_CERTIFICATE_WITH_NEW_TAG, actual2);
    }

    @Test
    void updateEntityShouldReturnResult() {
        GiftCertificate actual1 = certificateDao.save(UPDATE_GIFT_CERTIFICATE_1);
        assertEquals(UPDATE_GIFT_CERTIFICATE_1, actual1);

        GiftCertificate actual2 = certificateDao.save(UPDATE_GIFT_CERTIFICATE_2);
        assertEquals(UPDATE_GIFT_CERTIFICATE_2, actual2);
    }
}
