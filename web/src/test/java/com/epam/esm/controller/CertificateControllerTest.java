package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
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

import com.epam.esm.dto.TagDto;
import lombok.SneakyThrows;
import org.mockito.InjectMocks;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CertificateControllerTest {

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_1 = new GiftCertificateDto("1", "ATV riding",
            "Description ATV riding", "100", "10", LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-07T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("4", "atv")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_2 = new GiftCertificateDto("2", "Horse riding",
            "Horse riding description", "80", "8", LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-05T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("5", "horse")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_3 = new GiftCertificateDto("3", "Visiting a restaurant",
            "Visiting the Plaza restaurant", "50", "7", LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-02T14:15:13.257"), Arrays.asList(new TagDto("8", "food"), new TagDto("10", "restaurant"), new TagDto("12", "visit")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_4 = new GiftCertificateDto("4", "Visit to the drama theater",
            "Description visit to the drama theater", "45", "2", LocalDateTime.parse("2022-03-30T10:12:45.123"),
            LocalDateTime.parse("2022-04-08T14:15:13.257"), Arrays.asList(new TagDto("6", "theater"), new TagDto("12", "visit")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_5 = new GiftCertificateDto("5", "Shopping at the tool store",
            "Description shopping at the tool store", "30", "10", LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-04-01T14:15:13.257"), Arrays.asList(new TagDto("3", "shopping"), new TagDto("7", "tool")));

    private static final GiftCertificateDto GIFT_CERTIFICATE_DTO_3_NEW = new GiftCertificateDto("Visiting a restaurant",
            "Visiting the Plaza restaurant", "50", "7", Arrays.asList(new TagDto("food"), new TagDto("restaurant"), new TagDto("visit")));


    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GiftCertificateServiceImpl certificateService;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CertificateController certificateController;

    public CertificateControllerTest() {
    }

    @SneakyThrows
    @Test
    public void givenId_whenGetExistingCertificate_thenReturnStatus200andTag() {
        when(certificateService.findEntityById(1L)).thenReturn(GIFT_CERTIFICATE_DTO_1);
        mockMvc.perform(get("/certificates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.certificateId").value(GIFT_CERTIFICATE_DTO_1.getCertificateId()))
                .andExpect(jsonPath("$.name").value(GIFT_CERTIFICATE_DTO_1.getName()))
                .andExpect(jsonPath("$.description").value(GIFT_CERTIFICATE_DTO_1.getDescription()))
                .andExpect(jsonPath("$.price").value(GIFT_CERTIFICATE_DTO_1.getPrice()))
                .andExpect(jsonPath("$.duration").value(GIFT_CERTIFICATE_DTO_1.getDuration()))
                .andExpect(jsonPath("$.createDate").value(GIFT_CERTIFICATE_DTO_1.getCreateDate().toString()))
                .andExpect(jsonPath("$.lastUpdateDate").value(GIFT_CERTIFICATE_DTO_1.getLastUpdateDate().toString()))
                .andExpect(jsonPath("$.tags[0].id").value((GIFT_CERTIFICATE_DTO_1.getTags().get(0).getId())))
                .andExpect(jsonPath("$.tags[0].name").value((GIFT_CERTIFICATE_DTO_1.getTags().get(0).getName())))
                .andExpect(jsonPath("$.tags[1].id").value((GIFT_CERTIFICATE_DTO_1.getTags().get(1).getId())))
                .andExpect(jsonPath("$.tags[1].name").value((GIFT_CERTIFICATE_DTO_1.getTags().get(1).getName())))
                .andExpect(jsonPath("$.tags[2].id").value((GIFT_CERTIFICATE_DTO_1.getTags().get(2).getId())))
                .andExpect(jsonPath("$.tags[2].name").value((GIFT_CERTIFICATE_DTO_1.getTags().get(2).getName())));
        verify(certificateService, times(1)).findEntityById(1L);
    }

    @Test
    public void givenId_whenGetNotExistingCertificate_thenReturnStatus404anExceptionThrown() throws Exception {
        when(certificateService.findEntityById(1000000L)).thenThrow(new NoSuchEntityException("ex.noSuchEntity", " (id = 1000000)"));
        mockMvc.perform(get("/certificates/1000000"))
                .andExpect(status().isNotFound());
        verify(certificateService, times(1)).findEntityById(1000000L);
    }

    @Test
    public void givenId_whenGetCertificate_whenBadId_thenReturns400anExceptionThrown() throws Exception {
        mockMvc.perform(get("/certificates/1re"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void givenParamsList_whenGetListCertificates_thenReturnStatus200andListTags() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pageNumber", "2");
        params.add("rows", "3");
        List<GiftCertificateDto> certificates = Arrays.asList(GIFT_CERTIFICATE_DTO_3, GIFT_CERTIFICATE_DTO_4, GIFT_CERTIFICATE_DTO_5);
        when(certificateService.findListCertificates(params, 2, 3))
                .thenReturn(new ResourceDto<>(certificates, 2, 3, 3000));

        mockMvc.perform(get("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("pageNumber", "2")
                        .param("rows", "3"))
                .andExpect(jsonPath("$.resources", hasSize(3)))

                .andExpect(jsonPath("$.resources[0].certificateId", is(certificates.get(0).getCertificateId())))
                .andExpect(jsonPath("$.resources[0].name").value(certificates.get(0).getName()))
                .andExpect(jsonPath("$.resources[0].description").value(certificates.get(0).getDescription()))
                .andExpect(jsonPath("$.resources[0].price").value(certificates.get(0).getPrice()))
                .andExpect(jsonPath("$.resources[0].duration").value(certificates.get(0).getDuration()))
                .andExpect(jsonPath("$.resources[0].createDate").value(certificates.get(0).getCreateDate().toString()))
                .andExpect(jsonPath("$.resources[0].lastUpdateDate").value(certificates.get(0).getLastUpdateDate().toString()))
                .andExpect(jsonPath("$.resources[0].tags[0].id").value((certificates.get(0).getTags().get(0).getId())))
                .andExpect(jsonPath("$.resources[0].tags[0].name").value((certificates.get(0).getTags().get(0).getName())))
                .andExpect(jsonPath("$.resources[0].tags[1].id").value((certificates.get(0).getTags().get(1).getId())))
                .andExpect(jsonPath("$.resources[0].tags[1].name").value((certificates.get(0).getTags().get(1).getName())))
                .andExpect(jsonPath("$.resources[0].tags[2].id").value((certificates.get(0).getTags().get(2).getId())))
                .andExpect(jsonPath("$.resources[0].tags[2].name").value((certificates.get(0).getTags().get(2).getName())))

                .andExpect(jsonPath("$.resources[1].certificateId", is(certificates.get(1).getCertificateId())))
                .andExpect(jsonPath("$.resources[1].name").value(certificates.get(1).getName()))
                .andExpect(jsonPath("$.resources[1].description").value(certificates.get(1).getDescription()))
                .andExpect(jsonPath("$.resources[1].price").value(certificates.get(1).getPrice()))
                .andExpect(jsonPath("$.resources[1].duration").value(certificates.get(1).getDuration()))
                .andExpect(jsonPath("$.resources[1].createDate").value(certificates.get(1).getCreateDate().toString()))
                .andExpect(jsonPath("$.resources[1].lastUpdateDate").value(certificates.get(1).getLastUpdateDate().toString()))
                .andExpect(jsonPath("$.resources[1].tags[0].id").value((certificates.get(1).getTags().get(0).getId())))
                .andExpect(jsonPath("$.resources[1].tags[0].name").value((certificates.get(1).getTags().get(0).getName())))
                .andExpect(jsonPath("$.resources[1].tags[1].id").value((certificates.get(1).getTags().get(1).getId())))
                .andExpect(jsonPath("$.resources[1].tags[1].name").value((certificates.get(1).getTags().get(1).getName())))

                .andExpect(jsonPath("$.resources[2].certificateId", is(certificates.get(2).getCertificateId())))
                .andExpect(jsonPath("$.resources[2].name").value(certificates.get(2).getName()))
                .andExpect(jsonPath("$.resources[2].description").value(certificates.get(2).getDescription()))
                .andExpect(jsonPath("$.resources[2].price").value(certificates.get(2).getPrice()))
                .andExpect(jsonPath("$.resources[2].duration").value(certificates.get(2).getDuration()))
                .andExpect(jsonPath("$.resources[2].createDate").value(certificates.get(2).getCreateDate().toString()))
                .andExpect(jsonPath("$.resources[2].lastUpdateDate").value(certificates.get(2).getLastUpdateDate().toString()))
                .andExpect(jsonPath("$.resources[2].tags[0].id").value((certificates.get(2).getTags().get(0).getId())))
                .andExpect(jsonPath("$.resources[2].tags[0].name").value((certificates.get(2).getTags().get(0).getName())))
                .andExpect(jsonPath("$.resources[2].tags[1].id").value((certificates.get(2).getTags().get(1).getId())))
                .andExpect(jsonPath("$.resources[2].tags[1].name").value((certificates.get(2).getTags().get(1).getName())))

                .andExpect(jsonPath("$.pageNumber", is(2)))
                .andExpect(jsonPath("$.numberObjects", is(3)))
                .andExpect(jsonPath("$.totalNumberObjects", is(3000)));

        verify(certificateService, times(1)).findListCertificates(params, 2, 3);
    }

    @Test
    public void givenParamsList_whenGetListCertificates_whenBadValueParam_thenReturns400anExceptionThrown() throws Exception {
        mockMvc.perform(get("/certificates?rows=-3&pageNumber=42"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificate_whenCreateCertificate_thenReturnStatus201andCreatedTag() throws Exception {
        when(certificateService.createCertificate(GIFT_CERTIFICATE_DTO_3_NEW)).thenReturn(GIFT_CERTIFICATE_DTO_3);

        System.out.println(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_3_NEW));
        mockMvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_3_NEW)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.certificateId").value(GIFT_CERTIFICATE_DTO_3.getCertificateId()))
                .andExpect(jsonPath("$.name").value(GIFT_CERTIFICATE_DTO_3.getName()))
                .andExpect(jsonPath("$.description").value(GIFT_CERTIFICATE_DTO_3.getDescription()))
                .andExpect(jsonPath("$.price").value(GIFT_CERTIFICATE_DTO_3.getPrice()))
                .andExpect(jsonPath("$.duration").value(GIFT_CERTIFICATE_DTO_3.getDuration()))
                .andExpect(jsonPath("$.createDate").value(GIFT_CERTIFICATE_DTO_3.getCreateDate().toString()))
                .andExpect(jsonPath("$.lastUpdateDate").value(GIFT_CERTIFICATE_DTO_3.getLastUpdateDate().toString()))
                .andExpect(jsonPath("$.tags[0].name").value(GIFT_CERTIFICATE_DTO_3.getTags().get(0).getName()))
                .andExpect(jsonPath("$.tags[1].name").value(GIFT_CERTIFICATE_DTO_3.getTags().get(1).getName()));
        verify(certificateService, times(1)).createCertificate(GIFT_CERTIFICATE_DTO_3_NEW);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificate_whenCreateCertificate_whenBadValueBody_thenReturnStatus400anExceptionThrown() throws Exception {
        mockMvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificate_whenCreateCertificate_whenDuplicateName_thenReturnStatus400anExceptionThrown() throws Exception {
        when(certificateService.createCertificate(GIFT_CERTIFICATE_DTO_3_NEW)).thenThrow(new DuplicateEntityException("ex.duplicate", "rest )"));
        mockMvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_3_NEW)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificateAndId_whenUpdateCertificate_thenReturnStatus200andUpdatedTag() throws Exception {
        when(certificateService.updateCertificate(GIFT_CERTIFICATE_DTO_3_NEW, "3")).thenReturn(GIFT_CERTIFICATE_DTO_3);

        mockMvc.perform(patch("/certificates/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_3_NEW)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.certificateId").value(GIFT_CERTIFICATE_DTO_3.getCertificateId()))
                .andExpect(jsonPath("$.name").value(GIFT_CERTIFICATE_DTO_3.getName()))
                .andExpect(jsonPath("$.description").value(GIFT_CERTIFICATE_DTO_3.getDescription()))
                .andExpect(jsonPath("$.price").value(GIFT_CERTIFICATE_DTO_3.getPrice()))
                .andExpect(jsonPath("$.duration").value(GIFT_CERTIFICATE_DTO_3.getDuration()))
                .andExpect(jsonPath("$.createDate").value(GIFT_CERTIFICATE_DTO_3.getCreateDate().toString()))
                .andExpect(jsonPath("$.lastUpdateDate").value(GIFT_CERTIFICATE_DTO_3.getLastUpdateDate().toString()))
                .andExpect(jsonPath("$.tags[0].name").value(GIFT_CERTIFICATE_DTO_3.getTags().get(0).getName()))
                .andExpect(jsonPath("$.tags[1].name").value(GIFT_CERTIFICATE_DTO_3.getTags().get(1).getName()));
        verify(certificateService, times(1)).updateCertificate(GIFT_CERTIFICATE_DTO_3_NEW, "3");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificateAndId_whenUpdateNotExistingCertificate_thenReturnStatus404anExceptionThrown() throws Exception {
        when(certificateService.updateCertificate(GIFT_CERTIFICATE_DTO_3_NEW, "1000")).thenThrow(new NoSuchEntityException("ex.noSuchEntity", " (id = 1000)"));
        mockMvc.perform(patch("/certificates/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_3_NEW)))
                .andExpect(status().isNotFound());
        verify(certificateService, times(1)).updateCertificate(GIFT_CERTIFICATE_DTO_3_NEW, "1000");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificate_whenUpdateCertificate_whenBadValueBody_thenReturnStatus400anExceptionThrown() throws Exception {
        mockMvc.perform(patch("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_5)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenCertificateAndId_whenUpdateCertificate_whenDuplicateName_thenReturnStatus400anExceptionThrown() throws Exception {
        when(certificateService.updateCertificate(GIFT_CERTIFICATE_DTO_3_NEW, "1")).thenThrow(new DuplicateEntityException("ex.duplicate", "nature)"));
        mockMvc.perform(patch("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(GIFT_CERTIFICATE_DTO_3_NEW)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenId_whenDeleteCertificate_thenReturnStatus204() throws Exception {
        mockMvc.perform(delete("/certificates/2"))
                .andExpect(status().isNoContent());
        verify(certificateService, times(1)).deleteCertificate("2");

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenId_whenDeleteNotExistingCertificate_thenReturnStatus404anExceptionThrown() throws Exception {
        when(certificateService.findEntityById(1000L)).thenThrow(new NoSuchEntityException("ex.noSuchEntity", " (id = 1000)"));
        mockMvc.perform(get("/certificates/1000"))
                .andExpect(status().isNotFound());
    }
}
