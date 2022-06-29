package com.epam.esm.dto.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code CustomerMapper} generates code for mapping Tag entity and TagDto.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<Tag, TagDto> {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    /**
     * The method for converting dto to entity.
     *
     * @param entity Tag entity
     * @return converted dto
     */
    TagDto convertToDto(Tag entity);

    /**
     * The method for converting entity to dto.
     *
     * @param dto TagDto dto
     * @return converted entity
     */
    Tag convertToEntity(TagDto dto);
}
