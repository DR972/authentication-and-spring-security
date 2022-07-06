package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CERTIFICATE_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_GIFT_CERTIFICATE_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_GIFT_CERTIFICATE_LIST;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_TAG_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.LAST_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.NEXT_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.PAGE_1;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.PREVIOUS_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.TAG_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.allRequestParams;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class {@code GiftCertificateHateoasAdder} is implementation of interface {@link HateoasAdder}
 * and intended to work with {@link GiftCertificateDto} objects.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Component
public class GiftCertificateHateoasAdder implements HateoasAdder<GiftCertificateDto> {
    private static final String CREATE_GIFT_CERTIFICATE = "createGiftCertificate";
    private static final String UPDATE_GIFT_CERTIFICATE = "updateGiftCertificate";
    private static final String DELETE_GIFT_CERTIFICATE = "deleteGiftCertificate";

    @Override
    public void addLinks(GiftCertificateDto certificateDto) {
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(certificateDto.getCertificateId()))).withRel(GET_GIFT_CERTIFICATE_BY_ID));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, "5", "1")).withRel(GET_GIFT_CERTIFICATE_LIST));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).createCertificate(certificateDto)).withRel(CREATE_GIFT_CERTIFICATE));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).updateCertificate(String.valueOf(certificateDto.getCertificateId()), certificateDto)).withRel(UPDATE_GIFT_CERTIFICATE));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).deleteCertificate(String.valueOf(certificateDto.getCertificateId()))).withRel(DELETE_GIFT_CERTIFICATE));

        certificateDto.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel(GET_TAG_BY_ID)));
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<GiftCertificateDto> certificates, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) certificates.getTotalNumberObjects() / rows);
        certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(pageNumber), String.valueOf(rows))).withRel(GET_GIFT_CERTIFICATE_LIST));

        addSimpleResourceLinks(certificates, pageNumber, numberPages);
        addLinksToResourcesListPages(certificates, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<GiftCertificateDto> certificates, int pageNumber, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(certificates.getResources().get(0).getCertificateId())))
                    .withRel(GET_GIFT_CERTIFICATE_BY_ID));
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).createCertificate(certificates.getResources().get(0))).withRel(CREATE_GIFT_CERTIFICATE));
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).updateCertificate(String.valueOf(certificates.getResources().get(0).getCertificateId()), certificates.getResources().get(0)))
                    .withRel(UPDATE_GIFT_CERTIFICATE));
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).deleteCertificate(String.valueOf(certificates.getResources().get(0).getCertificateId())))
                    .withRel(DELETE_GIFT_CERTIFICATE));

            certificates.getResources().forEach(c -> {
                c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel(GET_GIFT_CERTIFICATE_BY_ID));
                c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel(GET_TAG_BY_ID)));
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<GiftCertificateDto> certificates, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, "1", String.valueOf(rows)))
                    .withRel(GET_GIFT_CERTIFICATE_LIST + PAGE_1));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel(GET_GIFT_CERTIFICATE_LIST + PREVIOUS_PAGE + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel(GET_GIFT_CERTIFICATE_LIST + NEXT_PAGE + (pageNumber + 1)));
            }
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel(GET_GIFT_CERTIFICATE_LIST + LAST_PAGE + numberPages));
        }
    }
}