package com.epam.esm.service.impl;

import com.epam.esm.dao.Dao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.EntityMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DeleteEntityException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.service.TagService;
import com.epam.esm.dto.TagDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The class {@code TagServiceImpl} is implementation of interface {@link TagService} and provides logic to work with {@link com.epam.esm.entity.Tag}.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Service
public class TagServiceImpl extends AbstractService<Tag, Long, TagDto> implements TagService {
    /**
     * TagDao tagDao.
     */
    private final TagDao tagDao;

    /**
     * GiftCertificateDao certificateDao.
     */
    private final GiftCertificateDao certificateDao;

    /**
     * The constructor creates a TagServiceImpl object
     *
     * @param dao            AbstractDao<Tag, Long> dao
     * @param entityMapper   EntityMapper<Tag, TagDto> entityMapper
     * @param tagDao         TagDao tagDao
     * @param certificateDao GiftCertificateDao certificateDao
     */
    public TagServiceImpl(Dao<Tag, Long> dao, EntityMapper<Tag, TagDto> entityMapper, TagDao tagDao, GiftCertificateDao certificateDao) {
        super(dao, entityMapper);
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagDao.findTagByName(name);
    }

    @Override
    @Transactional
    public TagDto createTag(TagDto tagDto) {
        Tag tag = entityMapper.convertToEntity(tagDto);
        if (findTagByName(tag.getName()).isPresent()) {
            throw new DuplicateEntityException("ex.duplicate", tag.getName());
        }
        return entityMapper.convertToDto(tagDao.save(tag));
    }

    @Override
    @Transactional
    public TagDto updateTag(TagDto tagDto, String id) {
        Tag tag = entityMapper.convertToEntity(tagDto);
        if (findTagByName(tag.getName()).isPresent()) {
            throw new DuplicateEntityException("ex.duplicate", tag.getName());
        }
        findEntityById(Long.parseLong(id));
        tag.setId(Long.valueOf(id));
        return entityMapper.convertToDto(tagDao.save(tag));
    }

    @Override
    @Transactional
    public void deleteTag(String id) {
        Tag tag = entityMapper.convertToEntity(findEntityById(Long.parseLong(id)));
        if (!certificateDao.findAllByTags_Id(Long.parseLong(id)).isEmpty()) {
            throw new DeleteEntityException("ex.deleteTag", tag.getName());
        } else {
            tagDao.deleteById(Long.parseLong(id));
        }
    }

    @Override
    public void deleteTagNotAssociatedWithCertificates(List<Tag> tags) {
        tagDao.deleteTagNotAssociatedWithCertificates(tags.stream().map(Tag::getName).collect(Collectors.joining(",", "'", "'")));
    }

    @Override
    public ResourceDto<TagDto> findMostPopularTag(int pageNumber, int limit) {
        List<TagDto> tags = tagDao.findMostPopularTag(PageRequest.of(pageNumber - 1, limit)).stream().map(entityMapper::convertToDto).collect(Collectors.toList());
        return new ResourceDto<>(tags, pageNumber, tags.size(), tagDao.countMostPopularTag());
    }
}