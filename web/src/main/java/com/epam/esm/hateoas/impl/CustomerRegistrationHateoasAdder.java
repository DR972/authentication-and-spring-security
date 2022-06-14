package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.CustomerCredentialDto;
import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.AUTHENTICATION_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CERTIFICATE_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.TAG_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.allRequestParams;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component("customerRegistrationHateoasAdder")
public class CustomerRegistrationHateoasAdder implements HateoasAdder<CustomerDto> {

    @Override
    public void addLinks(CustomerDto customerDto) {
        customerDto.add(linkTo(methodOn(AUTHENTICATION_CONTROLLER).authenticateCustomer(new CustomerCredentialDto())).withRel("authorizeCustomer"));
        customerDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById("1")).withRel("getGiftCertificateById"));
        customerDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, "5", "1")).withRel("getGiftCertificateList"));
        customerDto.add(linkTo(methodOn(TAG_CONTROLLER).getTagById("1")).withRel("getTagById"));
        customerDto.add(linkTo(methodOn(TAG_CONTROLLER).getTagList("5", "1")).withRel("getTagList"));
        customerDto.add(linkTo(methodOn(TAG_CONTROLLER).getMostPopularTags(String.valueOf(5), String.valueOf(1)))
                .withRel("getMostWidelyUsedTagsOfCustomersWithHighestCostOfAllOrders"));
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<CustomerDto> customers, int... params) {
    }
}
