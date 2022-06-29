package com.epam.esm.hateoas;

import com.epam.esm.dto.ResourceDto;
import org.springframework.hateoas.RepresentationModel;

/**
 * The interface {@code HateoasAdder} HateoasAdder describes abstract behavior for adding hateoas to objects.
 *
 * @param <T> the type parameter
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
public interface HateoasAdder<T extends RepresentationModel<T>> {

    /**
     * Method for adding links to entity object.
     *
     * @param entity entity to which links will be added
     */
    void addLinks(T entity);

    /**
     * Method for adding links for list entity objects.
     *
     * @param entities List<T> entities
     * @param params   int[] params
     */
    void addLinksToEntitiesList(ResourceDto<T> entities, int... params);
}
