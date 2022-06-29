package com.epam.esm.dto.mapper;

import org.springframework.stereotype.Component;

@Component
public interface EntityMapper<T, D> {

    /**
     * The method for converting dto to entity.
     *
     * @param entity T entity
     * @return converted dto
     */
    D convertToDto(T entity);

    /**
     * The method for converting entity to dto.
     *
     * @param dto T dto
     * @return converted entity
     */
    T convertToEntity(D dto);
}
