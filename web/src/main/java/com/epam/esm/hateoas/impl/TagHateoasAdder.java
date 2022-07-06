package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_MOST_POPULAR_TAGS;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_TAG_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_TAG_LIST;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.LAST_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.NEXT_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.PAGE_1;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.PREVIOUS_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.TAG_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class {@code TagHateoasAdder} is implementation of interface {@link HateoasAdder}
 * and intended to work with {@link TagDto} objects.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Component
public class TagHateoasAdder implements HateoasAdder<TagDto> {
    private static final String CREATE_TAG = "createTag";
    private static final String UPDATE_TAG = "updateTag";
    private static final String DELETE_TAG = "deleteTag";

    @Override
    public void addLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(tagDto.getId()))).withRel(GET_TAG_BY_ID));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).getTagList("5", "1")).withRel(GET_TAG_LIST));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).createTag(tagDto)).withRel(CREATE_TAG));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).updateTag(String.valueOf(tagDto.getId()), tagDto)).withRel(UPDATE_TAG));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).deleteTag(String.valueOf(tagDto.getId()))).withRel(DELETE_TAG));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).getMostPopularTags(String.valueOf(5), String.valueOf(1))).withRel(GET_MOST_POPULAR_TAGS));
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<TagDto> tags, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) tags.getTotalNumberObjects() / rows);
        tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(pageNumber), String.valueOf(rows))).withRel(GET_TAG_LIST));

        addSimpleResourceLinks(tags, pageNumber, numberPages);
        addLinksToResourcesListPages(tags, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<TagDto> tags, int pageNumber, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            tags.getResources().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel(GET_TAG_BY_ID)));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(tags.getResources().get(0).getId()))).withRel(GET_TAG_BY_ID));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).createTag(tags.getResources().get(0))).withRel(CREATE_TAG));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).updateTag(String.valueOf(tags.getResources().get(0).getId()), tags.getResources().get(0))).withRel(UPDATE_TAG));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).deleteTag(String.valueOf(tags.getResources().get(0).getId()))).withRel(DELETE_TAG));
        }
        tags.add(linkTo(methodOn(TAG_CONTROLLER).getMostPopularTags(String.valueOf(5), String.valueOf(1))).withRel(GET_MOST_POPULAR_TAGS));
    }

    private void addLinksToResourcesListPages(ResourceDto<TagDto> tags, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList("1", String.valueOf(rows))).withRel(GET_TAG_LIST + PAGE_1));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(pageNumber - 1), String.valueOf(rows))).withRel(GET_TAG_LIST + PREVIOUS_PAGE + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(pageNumber + 1), String.valueOf(rows))).withRel(GET_TAG_LIST + NEXT_PAGE + (pageNumber + 1)));
            }
            tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel(GET_TAG_LIST + LAST_PAGE + numberPages));
        }
    }
}