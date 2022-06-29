package com.epam.esm.controller;

import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.ResourceDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.impl.TagServiceImpl;
import lombok.SneakyThrows;
import org.mockito.InjectMocks;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {
    private static final TagDto TAG_DTO_1 = new TagDto("1", "rest");
    private static final TagDto TAG_DTO_2 = new TagDto("2", "nature");
    private static final TagDto TAG_DTO_3 = new TagDto("3", "shopping");

    private static final TagDto TAG_DTO_2_NEW = new TagDto("nature");

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TagServiceImpl tagService;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private TagController tagController;

    public TagControllerTest() {
    }

    @SneakyThrows
    @Test
    public void givenId_whenGetExistingTag_thenReturnStatus200andTag() {
        when(tagService.findEntityById(1L)).thenReturn(TAG_DTO_1);

        mockMvc.perform(get("/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("rest"));
        verify(tagService, times(1)).findEntityById(1L);
    }

    @Test
    public void givenId_whenGetNotExistingTag_thenReturnStatus404anExceptionThrown() throws Exception {
        when(tagService.findEntityById(1000L)).thenThrow(new NoSuchEntityException("ex.noSuchEntity", " (id = 1000)"));
        mockMvc.perform(get("/tags/1000"))
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).findEntityById(1000L);
    }

    @Test
    public void givenId_whenGetTag_whenBadId_thenReturns400anExceptionThrown() throws Exception {
        mockMvc.perform(get("/tags/1re"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void givenParamsList_whenGetListTags_thenReturnStatus200andListTags() {
        when(tagService.findListEntities(1, 3)).thenReturn(new ResourceDto<>(Arrays.asList(TAG_DTO_1, TAG_DTO_2, TAG_DTO_3), 1, 3, 3));

        mockMvc.perform(get("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "1")
                        .param("rows", "3"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.resources", hasSize(3)))
                .andExpect(jsonPath("$.resources[0].id", is("1")))
                .andExpect(jsonPath("$.resources[0].name", is("rest")))
                .andExpect(jsonPath("$.resources[1].id", is("2")))
                .andExpect(jsonPath("$.resources[1].name", is("nature")))
                .andExpect(jsonPath("$.resources[2].id", is("3")))
                .andExpect(jsonPath("$.resources[2].name", is("shopping")))
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.numberObjects", is(3)))
                .andExpect(jsonPath("$.totalNumberObjects", is(3)));

        verify(tagService, times(1)).findListEntities(1, 3);
    }

    @Test
    public void givenParamsList_whenGetListTags_whenBadValueParam_thenReturns400anExceptionThrown() throws Exception {
        mockMvc.perform(get("/tags?rows=-3&pageNumber=42"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTag_whenCreateTag_thenReturnStatus201andCreatedTag() throws Exception {
        when(tagService.createTag(TAG_DTO_2_NEW)).thenReturn(TAG_DTO_2);

        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2_NEW)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name", is("nature")));
        verify(tagService, times(1)).createTag(TAG_DTO_2_NEW);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTag_whenCreateTag_whenBadValueBody_thenReturnStatus400anExceptionThrown() throws Exception {
        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTag_whenCreateTag_whenDuplicateName_thenReturnStatus400anExceptionThrown() throws Exception {
        when(tagService.createTag(TAG_DTO_2_NEW)).thenThrow(new DuplicateEntityException("ex.duplicate", "rest )"));
        mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2_NEW)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTagAndId_whenUpdateTag_thenReturnStatus200andUpdatedTag() throws Exception {
        when(tagService.updateTag(TAG_DTO_2_NEW, "2")).thenReturn(TAG_DTO_2);

        mockMvc.perform(patch("/tags/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2_NEW)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name", is("nature")));
        verify(tagService, times(1)).updateTag(TAG_DTO_2_NEW, "2");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTagAndId_whenUpdateNotExistingTag_thenReturnStatus404anExceptionThrown() throws Exception {
        when(tagService.updateTag(TAG_DTO_2_NEW, "1000")).thenThrow(new NoSuchEntityException("ex.noSuchEntity", " (id = 1000)"));
        mockMvc.perform(patch("/tags/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2_NEW)))
                .andExpect(status().isNotFound());
        verify(tagService, times(1)).updateTag(TAG_DTO_2_NEW, "1000");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTag_whenUpdateTag_whenBadValueBody_thenReturnStatus400anExceptionThrown() throws Exception {
        mockMvc.perform(patch("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenTagAndId_whenUpdateTag_whenDuplicateName_thenReturnStatus400anExceptionThrown() throws Exception {
        when(tagService.updateTag(TAG_DTO_2_NEW, "1")).thenThrow(new DuplicateEntityException("ex.duplicate", "nature )"));
        mockMvc.perform(patch("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TAG_DTO_2_NEW)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenId_whenDeleteTag_thenReturnStatus204() throws Exception {
        mockMvc.perform(delete("/tags/2"))
                .andExpect(status().isNoContent());
        verify(tagService, times(1)).deleteTag("2");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenId_whenDeleteNotExistingTag_thenReturnStatus404anExceptionThrown() throws Exception {
        when(tagService.findEntityById(1000L)).thenThrow(new NoSuchEntityException("ex.noSuchEntity", " (id = 1000)"));
        mockMvc.perform(get("/tags/1000"))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void givenParamsList_whenGetListMostPopularTags_thenReturnStatus200andListTags() {
        when(tagService.findMostPopularTag(1, 3))
                .thenReturn(new ResourceDto<>(Arrays.asList(TAG_DTO_1, TAG_DTO_2), 1, 2, 2));

        mockMvc.perform(get("/tags/popular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "1")
                        .param("rows", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resources", hasSize(2)))
                .andExpect(jsonPath("$.resources[0].id", is("1")))
                .andExpect(jsonPath("$.resources[0].name", is("rest")))
                .andExpect(jsonPath("$.resources[1].id", is("2")))
                .andExpect(jsonPath("$.resources[1].name", is("nature")))
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.numberObjects", is(2)))
                .andExpect(jsonPath("$.totalNumberObjects", is(2)));
        verify(tagService, times(1)).findMostPopularTag(1, 3);
    }

    @Test
    public void givenParamsList_whenGetListMostPopularTags_whenBadValueParam_thenReturns400anExceptionThrown() throws Exception {
        mockMvc.perform(get("/tags?rows=-3&pageNumber=42"))
                .andExpect(status().isBadRequest());
    }
}
