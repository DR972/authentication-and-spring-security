package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.mapper.TagMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest(classes = TagServiceImpl.class)
public class TagServiceImplTest {
    private static final String REST = "rest";
    private static final String NEW = "new";
    private static final Tag TAG_1 = new Tag(1, "rest");
    private static final Tag TAG_2 = new Tag(2, "nature");
    private static final Tag TAG_3 = new Tag(3, "shopping");
    private static final Tag NEW_TAG = new Tag(0, NEW);
    private static final TagDto TAG_DTO_1 = new TagDto("1", "rest");
    private static final TagDto TAG_DTO_2 = new TagDto("2", "nature");
    private static final TagDto TAG_DTO_3 = new TagDto("3", "shopping");
    private static final TagDto NEW_DTO_TAG = new TagDto("0", NEW);

    @MockBean
    private TagDao tagDao;
    @MockBean
    private TagMapper tagMapper;
    @MockBean
    private GiftCertificateDao certificateDao;
    @Autowired
    private TagServiceImpl tagService;

    @Test
    void findTagByIdShouldReturnResult() {
        when(tagMapper.convertToDto(TAG_2)).thenReturn(TAG_DTO_2);
        when(tagDao.findById(2L)).thenReturn(Optional.of(TAG_2));
        when(tagDao.findById(2L)).thenReturn(Optional.of(TAG_2));
        tagService.findEntityById(2L);
        verify(tagDao, times(1)).findById(2L);
        assertEquals(TAG_DTO_2, tagService.findEntityById(2L));
    }

    @Test
    void findTagByIdShouldThrowException() {
        when(tagDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> tagService.findEntityById(2L));
        verify(tagDao, times(1)).findById(2L);
        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findTagByNameShouldReturnResult() {
        when(tagDao.findTagByName(REST)).thenReturn(Optional.of(TAG_1));
        tagService.findTagByName(REST);
        verify(tagDao, times(1)).findTagByName(REST);
        assertEquals(Optional.of(TAG_1), tagService.findTagByName(REST));
    }

    @Test
    void findListTagsShouldReturnResult() {
        when(tagMapper.convertToDto(TAG_1)).thenReturn(TAG_DTO_1);
        when(tagMapper.convertToDto(TAG_2)).thenReturn(TAG_DTO_2);
        when(tagMapper.convertToDto(TAG_3)).thenReturn(TAG_DTO_3);
        when(tagDao.count()).thenReturn(5L);
        when(tagDao.findAll(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(Arrays.asList(TAG_1, TAG_2, TAG_3)));
        tagService.findListEntities(1, 5);
        verify(tagDao, times(1)).findAll(PageRequest.of(0, 5));
        assertEquals(new ResourceDto<>(Arrays.asList(TAG_DTO_1, TAG_DTO_2, TAG_DTO_3), 1, 3, 5),
                tagService.findListEntities(1, 5));
    }

    @Test
    void createTagShouldReturnResult() {
        when(tagMapper.convertToEntity(NEW_DTO_TAG)).thenReturn(NEW_TAG);
        when(tagMapper.convertToDto(NEW_TAG)).thenReturn(NEW_DTO_TAG);
        when(tagDao.findTagByName(NEW)).thenReturn(Optional.empty());
        when(tagDao.save(NEW_TAG)).thenReturn(NEW_TAG);
        tagService.createTag(NEW_DTO_TAG);
        verify(tagDao, times(1)).save(NEW_TAG);
        assertEquals(NEW_DTO_TAG, tagService.createTag(NEW_DTO_TAG));
    }

    @Test
    void createTagShouldThrowException() {
        when(tagMapper.convertToEntity(TAG_DTO_1)).thenReturn(TAG_1);
        when(tagDao.findTagByName(REST)).thenReturn(Optional.of(TAG_1));
        Exception exception = assertThrows(DuplicateEntityException.class, () -> tagService.createTag(TAG_DTO_1));
        verify(tagDao, times(1)).findTagByName(REST);
        assertTrue(exception.getMessage().contains("ex.duplicate"));
    }

    @Test
    void updateTagShouldReturnResult() {
        when(tagMapper.convertToEntity(NEW_DTO_TAG)).thenReturn(NEW_TAG);
        when(tagMapper.convertToDto(TAG_3)).thenReturn(NEW_DTO_TAG);
        when(tagDao.findTagByName(NEW)).thenReturn(Optional.empty());
        when(tagDao.findById(3L)).thenReturn(Optional.of(TAG_3));
        when(tagDao.save(NEW_TAG)).thenReturn(TAG_3);
        tagService.updateTag(NEW_DTO_TAG, "3");
        verify(tagDao, times(1)).save(NEW_TAG);
        assertEquals(NEW_DTO_TAG, tagService.updateTag(NEW_DTO_TAG, "3"));
    }

    @Test
    void updateTagShouldThrowDuplicateEntityException() {
        when(tagMapper.convertToEntity(TAG_DTO_1)).thenReturn(TAG_1);
        when(tagDao.findTagByName(REST)).thenReturn(Optional.of(TAG_1));
        Exception exception = assertThrows(DuplicateEntityException.class, () -> tagService.updateTag(TAG_DTO_1, "2"));
        verify(tagDao, times(1)).findTagByName(REST);
        assertTrue(exception.getMessage().contains("ex.duplicate"));
    }

    @Test
    void updateTagShouldThrowNoSuchEntityException() {
        when(tagMapper.convertToEntity(TAG_DTO_1)).thenReturn(TAG_1);
        when(tagDao.findTagByName(TAG_1.getName())).thenReturn(Optional.empty());
        when(tagDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> tagService.updateTag(TAG_DTO_1, "2"));
        verify(tagDao, times(1)).findById(2L);
        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void deleteTagTest() {
        when(tagMapper.convertToDto(TAG_3)).thenReturn(TAG_DTO_3);
        when(tagMapper.convertToEntity(TAG_DTO_3)).thenReturn(TAG_3);
        when(tagDao.findById(3L)).thenReturn(Optional.of(TAG_3));
        tagService.deleteTag("3");
        verify(tagDao, times(1)).deleteById(3L);
    }

    @Test
    void deleteTagShouldThrowNoSuchEntityException() {
        when(tagDao.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NoSuchEntityException.class, () -> tagService.deleteTag("2"));
        verify(tagDao, times(1)).findById(2L);
        assertTrue(exception.getMessage().contains("ex.noSuchEntity"));
    }

    @Test
    void findMostPopularTagShouldReturnResult() {
        when(tagMapper.convertToDto(TAG_1)).thenReturn(TAG_DTO_1);
        when(tagMapper.convertToDto(TAG_2)).thenReturn(TAG_DTO_2);
        when(tagDao.countMostPopularTag()).thenReturn(2L);
        when(tagDao.findMostPopularTag(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(Arrays.asList(TAG_1, TAG_2)));
        tagService.findMostPopularTag(1, 5);
        verify(tagDao, times(1)).findMostPopularTag(PageRequest.of(0, 5));
        assertEquals(new ResourceDto<>(Arrays.asList(TAG_DTO_1, TAG_DTO_2), 1, 2, 2),
                tagService.findMostPopularTag(1, 5));
    }
}
