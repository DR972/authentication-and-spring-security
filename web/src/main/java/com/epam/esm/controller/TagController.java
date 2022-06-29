package com.epam.esm.controller;

import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.TagService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;

/**
 * Class {@code TagController} is an endpoint of the API which allows you to perform CRUD operations on tags.
 * Annotated by {@link RestController} without parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/tags".
 * Annotated by {@link Validated} without parameters  provides checking of constraints in method parameters.
 * So that {@code TagController} is accessed by sending request to /tags.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Data
@RestController
@RequestMapping("/tags")
@Validated
public class TagController {
    private static final String ROWS = "rows";
    private static final String PAGE_NUMBER = "pageNumber";
    /**
     * TagService tagService.
     */
    private final TagService tagService;
    /**
     * HateoasAdder<TagDto> hateoasAdder.
     */
    private final HateoasAdder<TagDto> hateoasAdder;

    /**
     * Method for getting TagDto by ID.
     *
     * @param id TagDto id
     * @return TagDto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto getTagById(@PathVariable @Positive(message = "ex.tagIdPositive") @Digits(integer = 9, fraction = 0, message = "ex.tagIdPositive") String id) {
        TagDto tagDto = tagService.findEntityById(Long.parseLong(id));
        hateoasAdder.addLinks(tagDto);
        return tagDto;
    }

    /**
     * Method for getting list of all TagDto objects.
     *
     * @param rows       number of lines per page (5 by default)
     * @param pageNumber page number(default 0)
     * @return ResourceDto<TagDto>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto<TagDto> getTagList(@RequestParam(name = PAGE_NUMBER, defaultValue = "1") @Positive(message = "ex.page")
                                          @Digits(integer = 6, fraction = 0, message = "ex.page") String pageNumber,
                                          @RequestParam(name = ROWS, defaultValue = "5") @Positive(message = "ex.rows")
                                          @Digits(integer = 6, fraction = 0, message = "ex.rows") String rows) {
        ResourceDto<TagDto> tags = tagService.findListEntities(Integer.parseInt(pageNumber), Integer.parseInt(rows));
        hateoasAdder.addLinksToEntitiesList(tags, Integer.parseInt(rows), Integer.parseInt(pageNumber));
        return tags;
    }

    /**
     * Method for saving new Tag.
     * Annotated by {@link Validated} with parameters TagDto.OnCreate.class provides validation of the fields of the TagDto object when creating.
     *
     * @param tag Tag
     * @return created TagDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Validated(TagDto.OnCreate.class) @RequestBody TagDto tag) {
        TagDto tagDto = tagService.createTag(tag);
        hateoasAdder.addLinks(tagDto);
        return tagDto;
    }

    /**
     * Method for updating Tag.
     * Annotated by {@link Validated} with parameters TagDto.OnCreate.class provides validation of the fields of the TagDto object when updating.
     *
     * @param tag new TagDto parameters
     * @param id  TagDto id
     * @return updated TagDto
     */
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDto updateTag(@PathVariable @Positive(message = "ex.tagIdPositive") @Digits(integer = 9, fraction = 0, message = "ex.tagIdPositive") String id,
                            @Validated(TagDto.OnUpdate.class) @RequestBody TagDto tag) {
        TagDto tagDto = tagService.updateTag(tag, id);
        hateoasAdder.addLinks(tagDto);
        return tagDto;
    }

    /**
     * Method for removing Tag by ID.
     *
     * @param id TagDto id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTag(@PathVariable @Positive(message = "ex.tagIdPositive")
                                          @Digits(integer = 9, fraction = 0, message = "ex.tagIdPositive") String id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * Method for getting list the most widely used tags Of Customers with the highest cost of all orders.
     *
     * @param rows       number of lines per page (5 by default)
     * @param pageNumber page number(default 0)
     * @return list of TagDto objects
     */

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto<TagDto> getMostPopularTags(@RequestParam(name = PAGE_NUMBER, defaultValue = "1") @Positive(message = "ex.page")
                                                  @Digits(integer = 6, fraction = 0, message = "ex.page") String pageNumber,
                                                  @RequestParam(name = ROWS, defaultValue = "5") @Positive(message = "ex.rows")
                                                  @Digits(integer = 6, fraction = 0, message = "ex.rows") String rows) {
        ResourceDto<TagDto> tags = tagService.findMostPopularTag(Integer.parseInt(pageNumber), Integer.parseInt(rows));
        hateoasAdder.addLinksToEntitiesList(tags, Integer.parseInt(rows), Integer.parseInt(pageNumber));
        return tags;
    }
}
