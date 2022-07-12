package com.epam.esm.service.impl;

import com.epam.esm.dao.Dao;
import com.epam.esm.dto.BaseEntityDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.EntityMapper;
import com.epam.esm.entity.BaseEntity;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class {@code AbstractService} is designed for basic work with database tables.
 *
 * @param <T> indicates that for this instantiation of the Service, will be used this type of Entity implementation.
 * @param <D> indicates that for this instantiation of the BaseService, will be used this type of EntityDto implementation.
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Service
@Scope("prototype")
public class AbstractService<T extends BaseEntity<I>, I, D extends BaseEntityDto> implements BaseService<I, D> {

    /**
     * AbstractDao<T, ID> dao.
     */
    protected final Dao<T, I> dao;

    /**
     * EntityMapper<T, D> entityMapper.
     */
    protected final EntityMapper<T, D> entityMapper;

    /**
     * The constructor creates a AbstractService object
     *
     * @param dao          AbstractDao<T, ID> dao
     * @param entityMapper EntityMapper<T, D> entityMapper
     */
    @Autowired
    public AbstractService(Dao<T, I> dao, EntityMapper<T, D> entityMapper) {
        this.dao = dao;
        this.entityMapper = entityMapper;
    }

    @Override
    public D findEntityById(I id) {
        return entityMapper.convertToDto(dao.findById(id).orElseThrow(() -> new NoSuchEntityException("ex.noSuchEntity", "i = " + id)));
    }

    @Override
    public ResourceDto<D> findListEntities(int pageNumber, int limit) {
        List<D> entities = dao.findAll(PageRequest.of(pageNumber - 1, limit)).stream().map(entityMapper::convertToDto).collect(Collectors.toList());
        return new ResourceDto<>(entities, pageNumber, entities.size(), dao.count());
    }
}

