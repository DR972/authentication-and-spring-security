package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.CustomerCredentialDto;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CERTIFICATE_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CUSTOMER_ORDER_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.TAG_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.allRequestParams;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerAuthorizationHateoasAdder implements HateoasAdder<CustomerCredentialDto> {

    @Override
    public void addLinks(CustomerCredentialDto entity) {
        entity.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById("1")).withRel("getGiftCertificateById"));
        entity.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, "5", "1")).withRel("getGiftCertificateList"));
        entity.add(linkTo(methodOn(TAG_CONTROLLER).getTagById("1")).withRel("getTagById"));
        entity.add(linkTo(methodOn(TAG_CONTROLLER).getTagList("5", "1")).withRel("getTagList"));
        entity.add(linkTo(methodOn(TAG_CONTROLLER).getMostPopularTags(String.valueOf(5), String.valueOf(1)))
                .withRel("getMostWidelyUsedTagsOfCustomersWithHighestCostOfAllOrders"));
        entity.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).createCustomerOrder(new CustomerOrderDto())).withRel("createCustomerOrder"));
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<CustomerCredentialDto> entities, int... params) {
    }
}
