package com.epam.esm.dto.mapper;

import com.epam.esm.dto.CustomerDto;
import com.epam.esm.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface {@code CustomerMapper} generates code for mapping Customer entity and CustomerDto.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Mapper(componentModel = "spring", uses = CustomerOrderMapper.class)
public interface CustomerMapper extends EntityMapper<Customer, CustomerDto> {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    /**
     * The method for converting dto to entity.
     *
     * @param entity Customer entity
     * @return converted dto
     */
    @Mapping(source = "id", target = "customerId")
    CustomerDto convertToDto(Customer entity);

    /**
     * The method for converting entity to dto.
     *
     * @param dto CustomerDto dto
     * @return converted entity
     */
    @Mapping(source = "customerId", target = "id")
    Customer convertToEntity(CustomerDto dto);
}
