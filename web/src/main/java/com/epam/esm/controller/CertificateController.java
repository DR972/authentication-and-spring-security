package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.GiftCertificateService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;

/**
 * Class {@code GiftCertificateController} is an endpoint of the API which allows you to perform CRUD operations on GiftCertificates.
 * Annotated by {@link RestController} without parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/certificates".
 * Annotated by {@link Validated} without parameters  provides checking of constraints in method parameters.
 * So that {@code GiftCertificateController} is accessed by sending request to /certificates.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Data
@RestController
@RequestMapping("/certificates")
@Validated
public class CertificateController {
    private static final String ROWS = "rows";
    private static final String PAGE_NUMBER = "pageNumber";
    /**
     * GiftCertificateService certificateService.
     */
    private final GiftCertificateService certificateService;
    /**
     * HateoasAdder<GiftCertificateDto> hateoasAdder.
     */
    private final HateoasAdder<GiftCertificateDto> hateoasAdder;

    /**
     * Method for getting GiftCertificateDto by ID.
     *
     * @param id GiftCertificateDto id
     * @return GiftCertificateDto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto getCertificateById(@PathVariable @Positive(message = "ex.giftCertificateIdPositive")
                                                 @Digits(integer = 9, fraction = 0, message = "ex.giftCertificateIdPositive") String id) {
        GiftCertificateDto certificateDto = certificateService.findEntityById(Long.parseLong(id));
        hateoasAdder.addLinks(certificateDto);
        return certificateDto;
    }

    /**
     * Method for getting list of GiftCertificateDto objects from request parameters.
     *
     * @param allRequestParams request parameters, which include the information needed for the search
     * @param rows             number of lines per page (5 by default)
     * @param pageNumber       page number(default 0)
     * @return ResourceDto<GiftCertificateDto>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto<GiftCertificateDto> getCertificateList(@RequestParam(required = false) MultiValueMap<String, String> allRequestParams,
                                                              @RequestParam(name = PAGE_NUMBER, defaultValue = "1") @Positive(message = "ex.page")
                                                              @Digits(integer = 6, fraction = 0, message = "ex.page") String pageNumber,
                                                              @RequestParam(name = ROWS, defaultValue = "5") @Positive(message = "ex.rows")
                                                              @Digits(integer = 6, fraction = 0, message = "ex.rows") String rows) {
        ResourceDto<GiftCertificateDto> certificates = certificateService.findListCertificates(allRequestParams, Integer.parseInt(pageNumber), Integer.parseInt(rows));
        hateoasAdder.addLinksToEntitiesList(certificates, Integer.parseInt(rows), Integer.parseInt(pageNumber));
        return certificates;
    }

    /**
     * Method for saving new GiftCertificate.
     * Annotated by {@link Validated} with parameters GiftCertificateDto.OnCreate.class provides validation of the fields
     * of the GiftCertificateDto object when creating.
     *
     * @param certificate GiftCertificateDto certificateDto
     * @return created GiftCertificateDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto createCertificate(@Validated(GiftCertificateDto.OnCreate.class) @RequestBody GiftCertificateDto certificate) {
        GiftCertificateDto certificateDto = certificateService.createCertificate(certificate);
        hateoasAdder.addLinks(certificateDto);
        return certificateDto;
    }

    /**
     * Method for updating GiftCertificate.
     * Annotated by {@link Validated} with parameters GiftCertificateDto.OnUpdate.class provides validation of the fields
     * of the GiftCertificateDto object when updating.
     *
     * @param certificate new GiftCertificateDto parameters
     * @param id          GiftCertificateDto id
     * @return updated GiftCertificateDto
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto updateCertificate(@PathVariable @Positive(message = "ex.giftCertificateIdPositive")
                                                @Digits(integer = 9, fraction = 0, message = "ex.giftCertificateIdPositive") String id,
                                                @Validated(GiftCertificateDto.OnUpdate.class) @RequestBody GiftCertificateDto certificate) {
        GiftCertificateDto certificateDto = certificateService.updateCertificate(certificate, id);
        hateoasAdder.addLinks(certificateDto);
        return certificateDto;
    }

    /**
     * Method for removing GiftCertificate by ID.
     *
     * @param id GiftCertificateDto id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCertificate(@PathVariable @Positive(message = "ex.giftCertificateIdPositive")
                                                  @Digits(integer = 9, fraction = 0, message = "ex.giftCertificateIdPositive") String id) {
        certificateService.deleteCertificate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}