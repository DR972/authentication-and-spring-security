package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
    private static final Class<CertificateController> CERTIFICATE_CONTROLLER = CertificateController.class;
    private static final Class<TagController> TAG_CONTROLLER = TagController.class;
    private static final String NAME = "name";
    private static final String TAG = "tag";
    private static final String SORTING = "sorting";
    private static final String DESCRIPTION = "description";
    private static final MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();

    static {
        allRequestParams.add(SORTING, "lastUpdateDate");
        allRequestParams.add(SORTING, "name");
        allRequestParams.add(SORTING, "id");
        allRequestParams.add(SORTING, "price");
        allRequestParams.add(TAG, "lobortis est");
        allRequestParams.add(TAG, "hendrerit at");
        allRequestParams.add(NAME, "suspend");
        allRequestParams.add(NAME, "ligula");
        allRequestParams.add(DESCRIPTION, "ipsum");
        allRequestParams.add(DESCRIPTION, "primis");
    }

    @Override
    public void addLinks(GiftCertificateDto certificateDto) {
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(certificateDto.getCertificateId()))).withRel("getGiftCertificateById"));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, "5", "1")).withRel("getGiftCertificateList"));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).createCertificate(certificateDto)).withRel("createGiftCertificate"));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).updateCertificate(String.valueOf(certificateDto.getCertificateId()), certificateDto)).withRel("updateGiftCertificate"));
        certificateDto.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).deleteCertificate(String.valueOf(certificateDto.getCertificateId()))).withRel("deleteGiftCertificate"));

        certificateDto.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<GiftCertificateDto> certificates, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) certificates.getTotalNumberObjects() / rows);
        certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(pageNumber), String.valueOf(rows))).withRel("getGiftCertificateList"));

        addSimpleResourceLinks(certificates, pageNumber, numberPages);
        addLinksToResourcesListPages(certificates, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<GiftCertificateDto> certificates, int pageNumber, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(certificates.getResources().get(0).getCertificateId())))
                    .withRel("getGiftCertificateById"));
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).createCertificate(certificates.getResources().get(0))).withRel("createGiftCertificate"));
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).updateCertificate(String.valueOf(certificates.getResources().get(0).getCertificateId()), certificates.getResources().get(0)))
                    .withRel("updateGiftCertificate"));
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).deleteCertificate(String.valueOf(certificates.getResources().get(0).getCertificateId())))
                    .withRel("deleteGiftCertificate"));

            certificates.getResources().forEach(c -> {
                c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel("getGiftCertificateById"));
                c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<GiftCertificateDto> certificates, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, "1", String.valueOf(rows)))
                    .withRel("getGiftCertificateList page 1"));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel("getGiftCertificateList previous page " + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel("getGiftCertificateList next page " + (pageNumber + 1)));
            }
            certificates.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateList(allRequestParams, String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel("getGiftCertificateList last page " + numberPages));
        }
    }
}