package com.epam.esm.service;

import com.epam.esm.dto.ResourceDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Optional;

/**
 * The interface {@code TagService} describes abstract behavior for working with {@link com.epam.esm.service.impl.TagServiceImpl} objects.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public interface TagService extends BaseService<Tag, Long, TagDto> {

    /**
     * The method finds Tag by name.
     *
     * @param name Tag name
     * @return Optional<Tag> object
     */
    Optional<Tag> findTagByName(String name);

    /**
     * The method performs the operation of saving Tag.
     *
     * @param tagDto TagDto
     * @return TagDto object
     */
    TagDto createTag(TagDto tagDto);

    /**
     * The method performs the operation of updating Tag.
     *
     * @param tagDto new TagDto parameters
     * @param id     TagDto id
     * @return TagDto object
     */
    TagDto updateTag(TagDto tagDto, String id);

    /**
     * The method performs the operation of deleting Tag.
     *
     * @param id Tag id
     */
    void deleteTag(String id);

    /**
     * The method removes tags that are not associated with certificates in the `Tag` table.
     *
     * @param tags List<Tag> tags
     */
    void deleteTagNotAssociatedWithCertificates(List<Tag> tags);

    /**
     * The method finds list the most widely used tags Of Customers with the highest cost of all orders.
     *
     * @param pageNumber int pageNumber
     * @param limit      int limit
     * @return ResourceDto<TagDto>
     */
    ResourceDto<TagDto> findMostPopularTag(int pageNumber, int limit);
}
