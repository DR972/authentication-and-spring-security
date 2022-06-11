package com.epam.esm.service;

import com.epam.esm.dto.BaseEntityDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.entity.BaseEntity;

/**
 * The interface {@code BaseService} describes abstract behavior for working with
 * {@link com.epam.esm.service.impl.AbstractService} in database.
 *
 * @param <T> indicates that for this instantiation of the BaseService, will be used this type of Entity implementation.
 * @param <D> indicates that for this instantiation of the BaseService, will be used this type of EntityDto implementation.
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public interface BaseService<T extends BaseEntity<ID>, ID, D extends BaseEntityDto> {

    /**
     * The method finds objects T in the table 'T' by id.
     *
     * @param id Long id
     * @return D object
     */
    D findEntityById(ID id);

    /**
     * The method finds list T objects in the table `T`.
     *
     * @param pageNumber int pageNumber
     * @param limit      int limit
     * @return list of ResourceDto<D> objects
     */
    ResourceDto<D> findListEntities(int pageNumber, int limit);
}
