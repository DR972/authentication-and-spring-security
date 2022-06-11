package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The class {@code GiftCertificateDto} represents GiftCertificateDto.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> implements BaseEntityDto {

    /**
     * The marker  interface {@code OnCreate} helps in providing validation of {@link com.epam.esm.dto.GiftCertificateDto} class
     * fields when creating a new GiftCertificateDto object
     */
    public interface OnCreate {
    }

    /**
     * The marker  interface {@code OnUpdate} helps in providing validation of {@link com.epam.esm.dto.GiftCertificateDto} class
     * fields when updating GiftCertificateDto object
     */
    public interface OnUpdate {
    }

    /**
     * GiftCertificateDto certificateId.
     */
    @NotNull(groups = CustomerOrderDto.OnCreate.class, message = "ex.certificatesNotNull")
    @Positive(groups = CustomerOrderDto.OnCreate.class, message = "ex.certificateIdPositive")
    @Digits(groups = CustomerOrderDto.OnCreate.class, integer = 6, fraction = 0, message = "ex.certificateIdPositive")
    @Null(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.certificateIdNull")
    private String certificateId;

    /**
     * GiftCertificateDto name.
     */
    @NotNull(groups = GiftCertificateDto.OnCreate.class, message = "ex.certificateNameNotNull")
    @Pattern(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, regexp = "^[A-Za-z]+[\\w+\\s?]+\\w${2,30}", message = "ex.certificateName")
    @Null(groups = CustomerOrderDto.OnCreate.class, message = "ex.certificateNameNull")
    private String name;

    /**
     * GiftCertificateDto description.
     */
    @NotNull(groups = GiftCertificateDto.OnCreate.class, message = "ex.descriptionNotNull")
    @Pattern(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, regexp = "^[A-Za-z]+[\\w+\\s?]+\\w${2,200}", message = "ex.description")
    @Null(groups = CustomerOrderDto.OnCreate.class, message = "ex.descriptionNull")
    private String description;

    /**
     * GiftCertificateDto price.
     */
    @NotNull(groups = GiftCertificateDto.OnCreate.class, message = "ex.priceNotNull")
    @Positive(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.pricePositive")
    @Digits(integer = 6, fraction = 2, groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.pricePositive")
    @Null(groups = CustomerOrderDto.OnCreate.class, message = "ex.priceNull")
    private String price;

    /**
     * GiftCertificateDto duration.
     */
    @NotNull(groups = GiftCertificateDto.OnCreate.class, message = "ex.durationNotNull")
    @Positive(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.durationPositive")
    @Digits(integer = 3, fraction = 0, groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.durationPositive")
    @Null(groups = CustomerOrderDto.OnCreate.class, message = "ex.durationNull")
    private String duration;

    /**
     * GiftCertificateDto createDate.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Null(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class, CustomerOrderDto.OnCreate.class}, message = "ex.certificateCreateDateNull")
    private LocalDateTime createDate;

    /**
     * GiftCertificateDto lastUpdateDate.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Null(groups = {GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class, CustomerOrderDto.OnCreate.class}, message = "ex.certificateLastUpdateDateNull")
    private LocalDateTime lastUpdateDate;

    /**
     * List<Tag> tags.
     */
    @Null(groups = CustomerOrderDto.OnCreate.class, message = "ex.tagsNull")
    @NotEmpty(groups = {GiftCertificateDto.OnCreate.class}, message = "ex.tagsNotNull")
    @Valid
    private List<TagDto> tags;

    /**
     * The constructor creates a GiftCertificateDto object
     *
     * @param name        String name
     * @param description String description
     * @param price       BigDecimal price
     * @param duration    int duration
     * @param tags        List<Tag> tags
     */
    public GiftCertificateDto(String name, String description, String price, String duration, List<TagDto> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }

    /**
     * The constructor creates a GiftCertificateDto object
     *
     * @param name           String name
     * @param description    String description
     * @param price          BigDecimal price
     * @param duration       int duration
     * @param createDate     LocalDateTime createDate
     * @param lastUpdateDate LocalDateTime lastUpdateDate
     * @param tags           List<Tag> tags
     */
    public GiftCertificateDto(String name, String description, String price, String duration, LocalDateTime createDate, LocalDateTime lastUpdateDate, List<TagDto> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }
}
