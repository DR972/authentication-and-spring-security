package com.epam.esm.service;

import com.epam.esm.dto.BaseEntityDto;
import com.epam.esm.dto.ResourceDto;

/**
 * The interface {@code BaseService} describes abstract behavior for working with
 * {@link com.epam.esm.service.impl.AbstractService} in database.
 *
 * @param <D> indicates that for this instantiation of the BaseService, will be used this type of EntityDto implementation.
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public interface BaseService<I, D extends BaseEntityDto> {

    /**
     * The method finds objects D in the table 'D' by id.
     *
     * @param id Long id
     * @return D object
     */
    D findEntityById(I id);

    /**
     * The method finds list D objects in the table `D`.
     *
     * @param pageNumber int pageNumber
     * @param limit      int limit
     * @return list of ResourceDto<D> objects
     */
    ResourceDto<D> findListEntities(int pageNumber, int limit);
}
