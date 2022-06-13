package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.GiftCertificateMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.DateHandler;
import com.epam.esm.dto.GiftCertificateDto;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.epam.esm.service.util.TestDataProvider.DATE_TIME;
import static com.epam.esm.service.util.TestDataProvider.DESCRIPTION;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_1;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_2;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_3;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_4;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_5;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_5_NEW;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_6;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_7;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_8;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_9;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_1;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_2;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_3;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_4;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_5;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_5_NEW;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_6;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_7;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_8;
import static com.epam.esm.service.util.TestDataProvider.GIFT_CERTIFICATE_DTO_9;
import static com.epam.esm.service.util.TestDataProvider.HORSE;
import static com.epam.esm.service.util.TestDataProvider.LAST_UPDATE_DATE;
import static com.epam.esm.service.util.TestDataProvider.LAST_UPDATE_DATE_SNAKE_CASE;
import static com.epam.esm.service.util.TestDataProvider.NAME;
import static com.epam.esm.service.util.TestDataProvider.REST;
import static com.epam.esm.service.util.TestDataProvider.RIDING;
import static com.epam.esm.service.util.TestDataProvider.SORTING;
import static com.epam.esm.service.util.TestDataProvider.TAG;
import static com.epam.esm.service.util.TestDataProvider.VISIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = GiftCertificateServiceImpl.class)
class GiftCertificateServiceImplTest {
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

        GiftCertificateDto actual = certificateService.findEntityById(2L);
        assertEquals(GIFT_CERTIFICATE_DTO_2, actual);
    }

    @Test
    void findCertificateByIdShouldThrowException() {
        when(certificateDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> certificateService.findEntityById(2L));
        verify(certificateDao, times(1)).findById(2L);

        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findListCertificatesWithoutTagFilterShouldReturnResult1() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_6, GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_1,
                GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_3, GIFT_CERTIFICATE_5, GIFT_CERTIFICATE_7);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_6, GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_1,
                GIFT_CERTIFICATE_DTO_2, GIFT_CERTIFICATE_DTO_3, GIFT_CERTIFICATE_DTO_5, GIFT_CERTIFICATE_DTO_7);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", "", "", PageRequest.of(0, 10, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", "", "")).thenReturn(7L);

        ResourceDto<GiftCertificateDto> actual = certificateService.findListCertificates(createParameterMap(Arrays.asList(null, null, null, Collections.singletonList(LAST_UPDATE_DATE)),
                certificates, certificateDtos), 1, 10);
        ResourceDto<GiftCertificateDto> expected = new ResourceDto<>(certificateDtos, 1, 7, 7);
        assertEquals(expected, actual);
    }

    @Test
    void findListCertificatesWithoutTagFilterShouldReturnResult2() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_3);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_3);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", VISIT, "", PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                ("", "", VISIT, "")).thenReturn(2L);

        ResourceDto<GiftCertificateDto> actual = certificateService.findListCertificates(createParameterMap(Arrays.asList(null, Collections.singletonList(VISIT), null, Collections.singletonList(LAST_UPDATE_DATE)),
                certificates, certificateDtos), 1, 5);
        ResourceDto<GiftCertificateDto> expected = new ResourceDto<>(certificateDtos, 1, 2, 2);
        assertEquals(expected, actual);
    }

    @Test
    void findListCertificatesWithoutTagFilterShouldReturnResult3() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_4, GIFT_CERTIFICATE_3);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_3);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                (VISIT, "", "", "", PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                (VISIT, "", "", "")).thenReturn(2L);

        ResourceDto<GiftCertificateDto> actual = certificateService.findListCertificates(createParameterMap(Arrays.asList(Collections.singletonList(VISIT), null, null, Collections.singletonList(LAST_UPDATE_DATE)),
                certificates, certificateDtos), 1, 5);
        ResourceDto<GiftCertificateDto> expected = new ResourceDto<>(certificateDtos, 1, 2, 2);
        assertEquals(expected, actual);
    }

    @Test
    void findListCertificatesWithTagFilterShouldReturnResult1() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_7);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2, GIFT_CERTIFICATE_DTO_7);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", "", "", REST, PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", "", "", REST)).thenReturn(3L);

        ResourceDto<GiftCertificateDto> actual = certificateService.findListCertificates(createParameterMap(Arrays.asList(null, null, Collections.singletonList(REST), Collections.singletonList(LAST_UPDATE_DATE)),
                certificates, certificateDtos), 1, 5);
        ResourceDto<GiftCertificateDto> expected = new ResourceDto<>(certificateDtos, 1, 3, 3);
        assertEquals(expected, actual);
    }

    @Test
    void findListCertificatesWithTagFilterShouldReturnResult2() {
        List<GiftCertificate> certificates = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_7);
        List<GiftCertificateDto> certificateDtos = Arrays.asList(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2, GIFT_CERTIFICATE_DTO_7);

        when(certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", RIDING, "", REST, PageRequest.of(0, 5, Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE), new Sort.Order(Sort.Direction.ASC, NAME))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                ("", "", RIDING, "", REST)).thenReturn(3L);

        ResourceDto<GiftCertificateDto> actual = certificateService.findListCertificates(createParameterMap(Arrays.asList(null, Collections.singletonList(RIDING), Collections.singletonList(REST), Arrays.asList(LAST_UPDATE_DATE, NAME)),
                certificates, certificateDtos), 1, 5);
        ResourceDto<GiftCertificateDto> expected = new ResourceDto<>(certificateDtos, 1, 3, 3);
        assertEquals(expected, actual);
    }

    @Test
    void findListCertificatesWithTwoTagFilterShouldReturnResult() {
        List<GiftCertificate> certificates = Collections.singletonList(GIFT_CERTIFICATE_2);
        List<GiftCertificateDto> certificateDtos = Collections.singletonList(GIFT_CERTIFICATE_DTO_2);

        when(certificateDao.findAll("", "", "", "", REST, HORSE, PageRequest.of(0, 5,
                Sort.by(new Sort.Order(Sort.Direction.ASC, LAST_UPDATE_DATE_SNAKE_CASE))))).thenReturn(new PageImpl<>(certificates));
        when(certificateDao.count("", "", "", "", REST, HORSE)).thenReturn(1L);

        ResourceDto<GiftCertificateDto> actual = certificateService.findListCertificates(createParameterMap(Arrays.asList(null, null, Arrays.asList(REST, HORSE),
                Collections.singletonList(LAST_UPDATE_DATE)), certificates, certificateDtos), 1, 5);
        ResourceDto<GiftCertificateDto> expected = new ResourceDto<>(certificateDtos, 1, 1, 1);
        assertEquals(expected, actual);
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

        GiftCertificateDto actual = certificateService.createCertificate(GIFT_CERTIFICATE_DTO_8);
        assertEquals(GIFT_CERTIFICATE_DTO_8, actual);
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

        GiftCertificateDto actual = certificateService.updateCertificate(GIFT_CERTIFICATE_DTO_9, "5");
        assertEquals(GIFT_CERTIFICATE_DTO_5_NEW, actual);
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
