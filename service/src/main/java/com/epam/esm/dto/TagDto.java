package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * The class {@code TagDto} represents TagDto.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto> implements BaseEntityDto {
    /**
     * The marker interface {@code OnCreate} helps in providing validation of {@link com.epam.esm.dto.TagDto} class
     * fields when creating a new TagDto object
     */
    public interface OnCreate {
    }

    /**
     * The marker  interface {@code OnUpdate} helps in providing validation of {@link com.epam.esm.dto.TagDto} class
     * fields when updating TagDto object
     */
    public interface OnUpdate {
    }

    /**
     * TagDto id.
     */
    @Null(groups = {TagDto.OnCreate.class, TagDto.OnUpdate.class, GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.tagIdNull")
    private String id;

    /**
     * TagDto name.
     */
    @Pattern(groups = {TagDto.OnCreate.class, TagDto.OnUpdate.class, GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class},
            regexp = "^[A-Za-z]+[\\w+\\s?]+\\w${2,30}", message = "ex.tagName")
    @NotNull(groups = {TagDto.OnCreate.class, TagDto.OnUpdate.class, GiftCertificateDto.OnCreate.class, GiftCertificateDto.OnUpdate.class}, message = "ex.tagNameNotNull")
    private String name;

    /**
     * The constructor creates a TagDto object
     *
     * @param name String name
     */
    public TagDto(String name) {
        this.name = name;
    }
}
