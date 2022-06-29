package com.epam.esm.dto.mapper;

import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.entity.CustomerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code CustomerMapper} generates code for mapping CustomerOrder entity and CustomerOrderDto.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Mapper(componentModel = "spring", uses = {GiftCertificateMapper.class, CustomerOrderMapper.class})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrder, CustomerOrderDto> {
    CustomerOrderMapper INSTANCE = Mappers.getMapper(CustomerOrderMapper.class);

    /**
     * The method for converting dto to entity.
     *
     * @param entity CustomerOrder entity
     * @return converted dto
     */
    @Mapping(source = "id", target = "orderId")
    CustomerOrderDto convertToDto(CustomerOrder entity);

    /**
     * The method for converting entity to dto.
     *
     * @param dto CustomerOrderDto dto
     * @return converted entity
     */
    @Mapping(source = "orderId", target = "id")
    CustomerOrder convertToEntity(CustomerOrderDto dto);
}
