package com.epam.esm.service.impl;

import com.epam.esm.dao.CustomerOrderDao;
import com.epam.esm.dao.Dao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.EntityMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DeleteEntityException;
import com.epam.esm.service.DateHandler;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.mapper.GiftCertificateMapper;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.SortValueValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class {@code GiftCertificateServiceImpl} is implementation of interface {@link GiftCertificateService}
 * and provides logic to work with {@link com.epam.esm.entity.GiftCertificate}.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Service
public class GiftCertificateServiceImpl extends AbstractService<GiftCertificate, Long, GiftCertificateDto> implements GiftCertificateService {
    private static final String NAME = "name";
    private static final String TAG = "tag";
    private static final String SORTING = "sorting";
    private static final String DESCRIPTION = "description";
    private static final String REGEX_PATTERN = "([a-z])([A-Z]+)";
    private static final String REPLACEMENT = "$1_$2";
    /**
     * SortValueValidator validator.
     */
    private final SortValueValidator validator;
    /**
     * GiftCertificateDao certificateDao.
     */
    private final GiftCertificateDao certificateDao;
    /**
     * DateHandler dateHandler.
     */
    private final DateHandler dateHandler;
    /**
     * TagService tagService.
     */
    private final TagService tagService;
    /**
     * TagService tagService.
     */
    private final CustomerOrderDao orderDao;
    /**
     * GiftCertificateMapper certificateMapper.
     */
    private final GiftCertificateMapper certificateMapper;

    /**
     * The constructor creates a GiftCertificateServiceImpl object
     *
     * @param dao               AbstractDao<Tag, Long> dao
     * @param entityMapper      EntityMapper<Tag, TagDto> entityMapper
     * @param validator         SortValueValidator
     * @param certificateDao    GiftCertificateDao certificateDao
     * @param dateHandler       DateHandler dateHandler
     * @param tagService        TagService tagService
     * @param orderDao          CustomerOrderDao orderDao
     * @param certificateMapper GiftCertificateMapper certificateMapper
     */
    @Autowired
    public GiftCertificateServiceImpl(Dao<GiftCertificate, Long> dao, EntityMapper<GiftCertificate, GiftCertificateDto> entityMapper,
                                      SortValueValidator validator, GiftCertificateDao certificateDao, DateHandler dateHandler,
                                      TagService tagService, CustomerOrderDao orderDao, GiftCertificateMapper certificateMapper) {
        super(dao, entityMapper);
        this.validator = validator;
        this.certificateDao = certificateDao;
        this.orderDao = orderDao;
        this.dateHandler = dateHandler;
        this.tagService = tagService;
        this.certificateMapper = certificateMapper;
    }

    @Override
    public ResourceDto<GiftCertificateDto> findListCertificates(MultiValueMap<String, String> params, int pageNumber, int limit) {
        if (params.get(SORTING) != null) {
            validator.validateSortType(params.get(SORTING));
        }
        List<String> names = searchByDescriptionOrName(params.get(NAME));
        List<String> descriptions = searchByDescriptionOrName(params.get(DESCRIPTION));

        if (params.get(TAG) == null) {
            List<GiftCertificateDto> entities = certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                            (names.get(0), names.get(1), descriptions.get(0), descriptions.get(1), PageRequest.of(pageNumber - 1, limit, Sort.by(buildSort(params.get(SORTING)))))
                    .stream().map(entityMapper::convertToDto).collect(Collectors.toList());
            return new ResourceDto<>(entities, pageNumber, entities.size(), certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
                    (names.get(0), names.get(1), descriptions.get(0), descriptions.get(1)));
        }

        if (params.get(TAG).size() == 1) {
            List<GiftCertificateDto> entities = certificateDao.findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                            (names.get(0), names.get(1), descriptions.get(0), descriptions.get(1), params.get(TAG).get(0), PageRequest.of(pageNumber - 1, limit, Sort.by(buildSort(params.get(SORTING)))))
                    .stream().map(entityMapper::convertToDto).collect(Collectors.toList());
            return new ResourceDto<>(entities, pageNumber, entities.size(), certificateDao.countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
                    (names.get(0), names.get(1), descriptions.get(0), descriptions.get(1), params.get(TAG).get(0)));
        }

        List<String> sortingList = convertCamelToSnake(params.get(SORTING));
        List<GiftCertificateDto> entities = certificateDao.findAll(names.get(0), names.get(1), descriptions.get(0), descriptions.get(1),
                        params.get(TAG).get(0), params.get(TAG).get(1), PageRequest.of(pageNumber - 1, limit, Sort.by(buildSort(sortingList))))
                .stream().map(entityMapper::convertToDto).collect(Collectors.toList());
        return new ResourceDto<>(entities, pageNumber, entities.size(), certificateDao.count(names.get(0), names.get(1),
                descriptions.get(0), descriptions.get(1), params.get(TAG).get(0), params.get(TAG).get(1)));

    }

    @Override
    @Transactional
    public GiftCertificateDto createCertificate(GiftCertificateDto certificateDto) {
        certificateDto.setCreateDate(dateHandler.getCurrentDate());
        certificateDto.setLastUpdateDate(dateHandler.getCurrentDate());
        GiftCertificate certificate = entityMapper.convertToEntity(certificateDto);
        certificate.setTags(createListTags(certificateDto.getTags()));
        return entityMapper.convertToDto(certificateDao.save(certificate));
    }

    @Override
    @Transactional
    public GiftCertificateDto updateCertificate(GiftCertificateDto certificateDto, String id) {
        GiftCertificate certificate = entityMapper.convertToEntity(findEntityById(Long.parseLong(id)));
        List<Tag> tags = new ArrayList<>(certificate.getTags());
        certificateMapper.updateGiftCertificateFromDto(certificateDto, certificate);

        certificate.setLastUpdateDate(dateHandler.getCurrentDate());
        certificate.setId(Long.valueOf(id));
        certificate.setTags(createListTags(certificateDto.getTags()));

        GiftCertificateDto updatedCertificate = entityMapper.convertToDto(certificateDao.save(certificate));
        tagService.deleteTagNotAssociatedWithCertificates(tags);
        return updatedCertificate;
    }

    @Override
    @Transactional
    public void deleteCertificate(String id) {
        GiftCertificate certificate = certificateMapper.convertToEntity(findEntityById(Long.parseLong(id)));
        if (!orderDao.findCustomerOrderByGiftCertificates(certificate).isEmpty()) {
            throw new DeleteEntityException("ex.deleteCertificate", id);
        }
        certificateDao.deleteById(Long.parseLong(id));
        tagService.deleteTagNotAssociatedWithCertificates(certificate.getTags());
    }

    private List<Sort.Order> buildSort(List<String> orders) {
        return orders != null ? (orders.stream().map(o -> o.startsWith("-") ? (new Sort.Order(Sort.Direction.DESC, o.substring(1))) : (new Sort.Order(Sort.Direction.ASC, o)))
                .collect(Collectors.toList())) : new ArrayList<>();
    }

    private List<String> searchByDescriptionOrName(List<String> queryParams) {
        return queryParams != null ? ((queryParams.size() == 1) ? (Arrays.asList(queryParams.get(0), "")) : queryParams) : Arrays.asList("", "");
    }

    private List<String> convertCamelToSnake(List<String> sortingParams) {
        return sortingParams != null ? (sortingParams.stream().map(s -> s.replaceAll(REGEX_PATTERN, REPLACEMENT).toLowerCase()).collect(Collectors.toList())) : new ArrayList<>();
    }

    private List<Tag> createListTags(List<TagDto> tags) {
        return tags.stream().map(t -> tagService.findTagByName(t.getName()).orElse(new Tag(t.getName()))).collect(Collectors.toList());
    }
}