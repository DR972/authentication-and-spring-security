package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

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
    private static final Class<TagController> TAG_CONTROLLER = TagController.class;

    @Override
    public void addLinks(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(tagDto.getId()))).withRel("getTagById"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).getTagList("5", "1")).withRel("getTagList"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).createTag(tagDto)).withRel("createTag"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).updateTag(String.valueOf(tagDto.getId()), tagDto)).withRel("updateTag"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).deleteTag(String.valueOf(tagDto.getId()))).withRel("deleteTag"));
        tagDto.add(linkTo(methodOn(TAG_CONTROLLER).getMostPopularTags(String.valueOf(5), String.valueOf(1)))
                .withRel("getMostWidelyUsedTagsOfCustomersWithHighestCostOfAllOrders"));
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<TagDto> tags, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) tags.getTotalNumberObjects() / rows);
        tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(pageNumber), String.valueOf(rows))).withRel("getListTags"));

        addSimpleResourceLinks(tags, pageNumber, numberPages);
        addLinksToResourcesListPages(tags, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<TagDto> tags, int pageNumber, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            tags.getResources().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(tags.getResources().get(0).getId()))).withRel("getTagById"));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).createTag(tags.getResources().get(0))).withRel("createTag"));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).updateTag(String.valueOf(tags.getResources().get(0).getId()), tags.getResources().get(0))).withRel("updateTag"));
            tags.add(linkTo(methodOn(TAG_CONTROLLER).deleteTag(String.valueOf(tags.getResources().get(0).getId()))).withRel("deleteTag"));
        }
        tags.add(linkTo(methodOn(TAG_CONTROLLER).getMostPopularTags(String.valueOf(5), String.valueOf(1)))
                .withRel("getMostWidelyUsedTagsOfCustomersWithHighestCostOfAllOrders"));
    }

    private void addLinksToResourcesListPages(ResourceDto<TagDto> tags, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList("1", String.valueOf(rows))).withRel("getTagList page 1"));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(pageNumber - 1), String.valueOf(rows))).withRel("getTagList previous page " + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(pageNumber + 1), String.valueOf(rows))).withRel("getTagList next page " + (pageNumber + 1)));
            }
            tags.add(linkTo(methodOn(TAG_CONTROLLER).getTagList(String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel("getTagList last page " + numberPages));
        }
    }
}