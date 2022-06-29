package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.util.MultiValueMap;

/**
 * The interface {@code GiftCertificateService} describes abstract behavior for working with
 * {@link com.epam.esm.service.impl.GiftCertificateServiceImpl} objects.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public interface GiftCertificateService extends BaseService<GiftCertificate, Long, GiftCertificateDto> {

    /**
     * The method finds list GiftCertificates.
     *
     * @param params     MultiValueMap<String, String> all request params
     * @param pageNumber int pageNumber
     * @param limit      int limit
     * @return ResourceDto<GiftCertificateDto>
     */
    ResourceDto<GiftCertificateDto> findListCertificates(MultiValueMap<String, String> params, int pageNumber, int limit);

    /**
     * The method performs the operation of saving GiftCertificate.
     *
     * @param certificateDto GiftCertificateDto
     * @return GiftCertificateDto object
     */
    GiftCertificateDto createCertificate(GiftCertificateDto certificateDto);

    /**
     * The method performs the operation of updating GiftCertificate.
     *
     * @param certificateDto new GiftCertificateDto parameters
     * @param id             GiftCertificateDto id
     * @return GiftCertificateDto object
     */
    GiftCertificateDto updateCertificate(GiftCertificateDto certificateDto, String id);

    /**
     * The method performs the operation of deleting GiftCertificate.
     *
     * @param id GiftCertificateDto id
     */
    void deleteCertificate(String id);
}
