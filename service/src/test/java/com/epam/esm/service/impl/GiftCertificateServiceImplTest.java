package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.DateHandler;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.SortValueValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = GiftCertificateServiceImpl.class)
class GiftCertificateServiceImplTest {
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAG = "tag";
    private static final String SORTING = "sorting";
    private static final String LAST_UPDATE_DATE = "lastUpdateDate";
    private static final String HORSE = " horse";
    private static final String RIDING = "riding";
    private static final String VISIT = "visit";
    private static final String REST = "rest";
    private static final LocalDateTime DATE_TIME = LocalDateTime.parse("2022-05-01T00:00:00.001");

    private static final GiftCertificate GIFT_CERTIFICATE_1 = new GiftCertificate(1, "ATV riding",
            "Description ATV riding", new BigDecimal("100"), 10, LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-07T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(4, "atv")));

    private static final GiftCertificate GIFT_CERTIFICATE_2 = new GiftCertificate(2, "Horse riding",
            "Horse riding description", new BigDecimal("80"), 8, LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-05T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(5, "horse")));

    private static final GiftCertificate GIFT_CERTIFICATE_3 = new GiftCertificate(3, "Visiting a restaurant",
            "Visiting the Plaza restaurant", new BigDecimal("50"), 7, LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-02T14:15:13.257"), Arrays.asList(new Tag(8, "food"), new Tag(10, "restaurant"), new Tag(12, "visit")));

    private static final GiftCertificate GIFT_CERTIFICATE_4 = new GiftCertificate(4, "Visit to the drama theater",
            "Description visit to the drama theater", new BigDecimal("45"), 2, LocalDateTime.parse("2022-03-30T10:12:45.123"),
            LocalDateTime.parse("2022-04-08T14:15:13.257"), Arrays.asList(new Tag(6, "theater"), new Tag(12, "visit")));

    private static final GiftCertificate GIFT_CERTIFICATE_5 = new GiftCertificate(5, "Shopping at the tool store",
            "Description shopping at the tool store", new BigDecimal("30"), 10, LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-04-01T14:15:13.257"), Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool")));

    private static final GiftCertificate GIFT_CERTIFICATE_6 = new GiftCertificate(6, "Shopping at the supermarket",
            "Shopping at Lidl supermarket chain", new BigDecimal("80"), 12, LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-14T14:15:13.257"), Arrays.asList(new Tag(6, "shopping"), new Tag(8, "food"), new Tag(9, "supermarket")));

    private static final GiftCertificate GIFT_CERTIFICATE_7 = new GiftCertificate(7, "Hot air balloon flight",
            "An unforgettable hot air balloon flight", new BigDecimal("150"), 12, LocalDateTime.parse("2022-03-01T10:12:45.123"),
            LocalDateTime.parse("2022-03-14T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(11, "flight")));

    private static final GiftCertificate GIFT_CERTIFICATE_8 = new GiftCertificate("new GiftCertificate",
            "new description", new BigDecimal("10"), 10, LocalDateTime.parse("2022-05-01T00:00:00.001"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new Tag("rest"), new Tag("nature"), new Tag("new")));

    private static final GiftCertificate GIFT_CERTIFICATE_9 = new GiftCertificate(5, null,
            "Description shopping at the tool store", null, 10, null,
            null, Arrays.asList(new Tag("shopping"), new Tag("tool"), new Tag("new")));

    private static final GiftCertificate GIFT_CERTIFICATE_5_NEW = new GiftCertificate(5, "Shopping at the tool store",
            "Description shopping at the tool store", new BigDecimal("30"), 10, LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool"), new Tag("new")));


    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_1 = new GiftCertificateDto("1", "ATV riding",
            "Description ATV riding", "100", "10", LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-07T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("4", "atv")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_2 = new GiftCertificateDto("2", "Horse riding",
            "Horse riding description", "80", "8", LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-05T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("5", "horse")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_3 = new GiftCertificateDto("3", "Visiting a restaurant",
            "Visiting the Plaza restaurant", "50", "7", LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-02T14:15:13.257"), Arrays.asList(new TagDto("8", "food"), new TagDto("10", "restaurant"), new TagDto("12", "visit")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_4 = new GiftCertificateDto("4", "Visit to the drama theater",
            "Description visit to the drama theater", "45", "2", LocalDateTime.parse("2022-03-30T10:12:45.123"),
            LocalDateTime.parse("2022-04-08T14:15:13.257"), Arrays.asList(new TagDto("6", "theater"), new TagDto("12", "visit")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_5 = new GiftCertificateDto("5", "Shopping at the tool store",
            "Description shopping at the tool store", "30", "10", LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-04-01T14:15:13.257"), Arrays.asList(new TagDto("3", "shopping"), new TagDto("7", "tool")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_6 = new GiftCertificateDto("6", "Shopping at the supermarket",
            "Shopping at Lidl supermarket chain", "80", "12", LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-14T14:15:13.257"), Arrays.asList(new TagDto("6", "shopping"), new TagDto("8", "food"), new TagDto("9", "supermarket")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_7 = new GiftCertificateDto("7", "Hot air balloon flight",
            "An unforgettable hot air balloon flight", "150", "12", LocalDateTime.parse("2022-03-01T10:12:45.123"),
            LocalDateTime.parse("2022-03-14T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("11", "flight")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_8 = new GiftCertificateDto("new GiftCertificate",
            "new description", "10", "10", LocalDateTime.parse("2022-05-01T00:00:00.001"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new TagDto("rest"), new TagDto("nature"), new TagDto("new")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_9 = new GiftCertificateDto(null,
            "Description shopping at the tool store", null, "10", null,
            null, Arrays.asList(new TagDto("shopping"), new TagDto("tool"), new TagDto("new")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_5_NEW = new GiftCertificateDto("5", "Shopping at the tool store",
            "Description shopping at the tool store", "30", "10", LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new TagDto("3", "shopping"), new TagDto("7", "tool"), new TagDto("15", "new")));

    @MockBean
    private GiftCertificateDao certificateDao;
    @MockBean
    private TagService tagService;
    @MockBean
    private SortValueValidator validator;
    @MockBean
    private DateHandler dateHandler;
    @MockBean
    private GiftCertificateMapper certificateMapper;
    @MockBean
    private CustomerOrderDao orderDao;
    @Autowired
    private GiftCertificateServiceImpl certificateService;

    @Test
    void findCertificateByIdShouldReturnResult() {
        when(certificateMapper.convertToDto(GIFT_CERTIFICATE_2)).thenReturn(GIFT_CERTIFICATE_DTO_2);
        when(certificateDao.findById(2L)).thenReturn(Optional.of(GIFT_CERTIFICATE_2));
        certificateService.findEntityById(2L);
        verify(certificateDao, times(1)).findById(2L);
        assertEquals(GIFT_CERTIFICATE_DTO_2, certificateService.findEntityById(2L));
    }

    @Test
    void findCertificateByIdShouldThrowException() {
        when(certificateDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> certificateService.findEntityById(2L));
        verify(certificateDao, times(1)).findById(2L);
        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findListCertificatesWithoutTagFilterShouldReturnResult() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_6, GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_1,
                GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_3, GIFT_CERTIFICATE_5, GIFT_CERTIFICATE_7);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_6, GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_1,
                GIFT_CERTIFICATE_DTO_2, GIFT_CERTIFICATE_DTO_3, GIFT_CERTIFICATE_DTO_5, GIFT_CERTIFICATE_DTO_7);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", "", "", PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", "", "")).thenReturn(7L);

        assertEquals(new ResourceDto<>(certificateDtos, 1, 7, 7),
                certificateService.findListCertificates(createParameterMap(Arrays.asList(null, null, null, Collections.singletonList(LAST_UPDATE_DATE)),
                        certificates, certificateDtos), 1, 10));
    }

    @Test
    void findListCertificatesWithoutTagFilterShouldReturnResult2() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_3);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_3);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", VISIT, "", PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", VISIT, "")).thenReturn(2L);

        assertEquals(new ResourceDto<>(certificateDtos, 1, 2, 2),
                certificateService.findListCertificates(createParameterMap(Arrays.asList(null, Collections.singletonList(VISIT), null, Collections.singletonList(LAST_UPDATE_DATE)),
                        certificates, certificateDtos), 1, 5));
    }

    @Test
    void findListCertificatesWithoutTagFilterShouldReturnResult3() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_3);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_3);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                (VISIT, "", "", "", PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                (VISIT, "", "", "")).thenReturn(2L);

        assertEquals(new ResourceDto<>(certificateDtos, 1, 2, 2),
                certificateService.findListCertificates(createParameterMap(Arrays.asList(Collections.singletonList(VISIT), null, null, Collections.singletonList(LAST_UPDATE_DATE)),
                        certificates, certificateDtos), 1, 5));
    }

    @Test
    void findListCertificatesWithTagFilterShouldReturnResult() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_7);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2, GIFT_CERTIFICATE_DTO_7);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", "", "", REST, PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", "", "", REST)).thenReturn(3L);

        assertEquals(new ResourceDto<>(certificateDtos, 1, 3, 3),
                certificateService.findListCertificates(createParameterMap(Arrays.asList(null, null, Collections.singletonList(REST), Collections.singletonList(LAST_UPDATE_DATE)),
                        certificates, certificateDtos), 1, 5));
    }

    @Test
    void findListCertificatesWithTagFilterShouldReturnResult2() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_7);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2, GIFT_CERTIFICATE_DTO_7);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", RIDING, "", REST, PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE), new Sort.Order(Sort.Direction.ASC, NAME))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", RIDING, "", REST)).thenReturn(3L);

        assertEquals(new ResourceDto<>(certificateDtos, 1, 3, 3),
                certificateService.findListCertificates(createParameterMap(Arrays.asList(null, Collections.singletonList(RIDING), Collections.singletonList(REST), Arrays.asList(LAST_UPDATE_DATE, NAME)),
                        certificates, certificateDtos), 1, 5));
    }

    @Test
    void findListCertificatesWithTwoTagFilterShouldReturnResult() {
        List<GiftCertificate> certificates = Collections.singletonList(GIFT_CERTIFICATE_2);
        List<GiftCertificateDto> certificateDtos = Collections.singletonList(GIFT_CERTIFICATE_DTO_2);

        when(certificateDao.findAll("", "", "", "", REST, HORSE, PageRequest.of(0, 5,
                Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.count("", "", "", "", REST, HORSE)).thenReturn(1L);

        assertEquals(new ResourceDto<>(certificateDtos, 1, 1, 1),
                certificateService.findListCertificates(createParameterMap(Arrays.asList(null, null, Arrays.asList(REST, HORSE),
                        Collections.singletonList(LAST_UPDATE_DATE)), certificates, certificateDtos), 1, 5));
    }

    private MultiValueMap<String, String> createParameterMap(List<List<String>> list, List<GiftCertificate> certificates, List<GiftCertificateDto> certificateDtos) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (list.get(0) != null) {
            params.put(NAME, list.get(0));
        }
        if (list.get(1) != null) {
            params.put(DESCRIPTION, list.get(1));
        }
        if (list.get(2) != null) {
            params.put(TAG, list.get(2));
        }
        if (list.get(3) != null) {
            params.put(SORTING, list.get(3));
        }
        IntStream.range(0, certificates.size()).forEach(i -> when(certificateMapper.convertToDto(certificates.get(i))).thenReturn(certificateDtos.get(i)));
        return params;
    }

    @Test
    void createCertificateShouldReturnResult() {
        when(dateHandler.getCurrentDate()).thenReturn(DATE_TIME);
        IntStream.range(0, GIFT_CERTIFICATE_8.getTags().size()).forEach(i ->
                when(tagService.findTagByName(GIFT_CERTIFICATE_DTO_8.getTags().get(i).getName())).thenReturn(Optional.ofNullable(GIFT_CERTIFICATE_8.getTags().get(i))));
        when(certificateMapper.convertToEntity(GIFT_CERTIFICATE_DTO_8)).thenReturn(GIFT_CERTIFICATE_8);
        when(certificateMapper.convertToDto(GIFT_CERTIFICATE_8)).thenReturn(GIFT_CERTIFICATE_DTO_8);
        when(certificateDao.save(GIFT_CERTIFICATE_8)).thenReturn(GIFT_CERTIFICATE_8);

        certificateService.createCertificate(GIFT_CERTIFICATE_DTO_8);
        verify(certificateDao, times(1)).save(GIFT_CERTIFICATE_8);
        assertEquals(GIFT_CERTIFICATE_DTO_8, certificateService.createCertificate(GIFT_CERTIFICATE_DTO_8));
    }

    @Test
    void updateCertificateShouldReturnResult() {
        when(certificateMapper.convertToEntity(GIFT_CERTIFICATE_DTO_5)).thenReturn(GIFT_CERTIFICATE_5);
        when(certificateMapper.convertToDto(GIFT_CERTIFICATE_5)).thenReturn(GIFT_CERTIFICATE_DTO_5);
        when(certificateDao.findById(5L)).thenReturn(Optional.of(GIFT_CERTIFICATE_5));
        when(dateHandler.getCurrentDate()).thenReturn(DATE_TIME);

        IntStream.range(0, GIFT_CERTIFICATE_DTO_9.getTags().size()).forEach(i ->
                when(tagService.findTagByName(GIFT_CERTIFICATE_DTO_9.getTags().get(i).getName())).thenReturn(Optional.ofNullable(GIFT_CERTIFICATE_9.getTags().get(i))));

        when(certificateMapper.convertToDto(GIFT_CERTIFICATE_5_NEW)).thenReturn(GIFT_CERTIFICATE_DTO_5_NEW);
        when(certificateDao.save(GIFT_CERTIFICATE_5)).thenReturn(GIFT_CERTIFICATE_5_NEW);
        assertEquals(GIFT_CERTIFICATE_DTO_5_NEW, certificateService.updateCertificate(GIFT_CERTIFICATE_DTO_9, "5"));
    }

    @Test
    void updateCertificateShouldThrowException() {
        when(certificateDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> certificateService.updateCertificate(GIFT_CERTIFICATE_DTO_5, "2"));
        verify(certificateDao, times(1)).findById(2L);
        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void deleteCertificateTest() {
        when(certificateMapper.convertToDto(GIFT_CERTIFICATE_3)).thenReturn(GIFT_CERTIFICATE_DTO_3);
        when(certificateMapper.convertToEntity(GIFT_CERTIFICATE_DTO_3)).thenReturn(GIFT_CERTIFICATE_3);
        when(certificateDao.findById(3L)).thenReturn(Optional.of(GIFT_CERTIFICATE_3));
        when(orderDao.findCustomerOrderByGiftCertificates(GIFT_CERTIFICATE_3)).thenReturn(new ArrayList<>());
        certificateService.deleteCertificate("3");
        verify(certificateDao, times(1)).deleteById(3L);
        verify(tagService, times(1)).deleteTagNotAssociatedWithCertificates(GIFT_CERTIFICATE_3.getTags());
    }

    @Test
    void deleteCertificateShouldThrowException() {
        when(certificateDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> certificateService.deleteCertificate("2"));
        verify(certificateDao, times(1)).findById(2L);
        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }
}
