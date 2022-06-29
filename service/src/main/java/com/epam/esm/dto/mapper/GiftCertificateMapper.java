package com.epam.esm.dto.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code CustomerMapper} generates code for mapping GiftCertificate entity and GiftCertificateDto.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface GiftCertificateMapper extends EntityMapper<GiftCertificate, GiftCertificateDto> {
    GiftCertificateMapper INSTANCE = Mappers.getMapper(GiftCertificateMapper.class);

    /**
     * The method for converting dto to entity.
     *
     * @param entity GiftCertificate entity
     * @return converted dto
     */
    @Mapping(source = "id", target = "certificateId")
    @Mapping(source = "price", target = "price", numberFormat = "#.00")
    GiftCertificateDto convertToDto(GiftCertificate entity);

    /**
     * The method for converting entity to dto.
     *
     * @param dto GiftCertificateDto dto
     * @return converted entity
     */
    @Mapping(source = "certificateId", target = "id")
    @Mapping(source = "price", target = "price", numberFormat = "#.00")
    GiftCertificate convertToEntity(GiftCertificateDto dto);

    /**
     * The method for updating an entity from dto.
     *
     * @param dto             GiftCertificateDto dto
     * @param giftCertificate GiftCertificate giftCertificate
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGiftCertificateFromDto(GiftCertificateDto dto, @MappingTarget GiftCertificate giftCertificate);
}
