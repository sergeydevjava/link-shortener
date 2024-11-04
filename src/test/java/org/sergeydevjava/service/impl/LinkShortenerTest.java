package org.sergeydevjava.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;
import org.sergeydevjava.dto.common.CommonRequest;
import org.sergeydevjava.dto.common.CommonResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LinkShortenerTest extends AbstractTest {

    public static final String API_V_1_LINK_INFOS = "/api/v1/link-infos";

    @BeforeEach
    void deleteExistingLinks() throws Exception {
        for (LinkInfoResponse lir : findLinkByFilter().getBody()) {
            performLinkDeletion(lir.getId().toString());
        }
    }

    @Test
    void createLink() throws Exception {
        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request.json");

        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        ResultActions getByShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/short-link/" + linkInfoResponseCommonResponse.getBody().getShortLink()))
                .andExpect(MockMvcResultMatchers.status().isTemporaryRedirect());

        String link = parseHeader(getByShortLinkResult.andReturn(), HttpHeaders.LOCATION);

        assertEquals(linkInfoResponseCommonResponse.getBody().getLink(), link);
    }

    @Test
    void updateLink() throws Exception {

        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request.json");
        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        CommonResponse<List<LinkInfoResponse>> listCommonResponse = findLinkByFilter();
        assertEquals(listCommonResponse.getBody().size(), 1L);
        assertEquals(listCommonResponse.getBody().getFirst().getDescription(), "google");

        updateLinkDescription(listCommonResponse);

        CommonResponse<List<LinkInfoResponse>> listCommonResponseAfterUpdate = findLinkByFilter();
        assertEquals(listCommonResponseAfterUpdate.getBody().size(), 1L);
        assertEquals(listCommonResponseAfterUpdate.getBody().getFirst().getDescription(), "yandex");

    }

    @Test
    void deleteLink() throws Exception {

        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request.json");
        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        CommonResponse<List<LinkInfoResponse>> listCommonResponse = findLinkByFilter();
        assertEquals(listCommonResponse.getBody().size(), 1L);

        performLinkDeletion(listCommonResponse.getBody().getFirst().getId().toString());

        CommonResponse<List<LinkInfoResponse>> listCommonResponseAfterUpdate = findLinkByFilter();
        assertEquals(listCommonResponseAfterUpdate.getBody().size(), 0L);

    }

    @Test
    void createInactiveLink() throws Exception {
        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/inactive-link-info-request.json");

        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        ResultActions getByShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/short-link/" + linkInfoResponseCommonResponse.getBody().getShortLink()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createOutdatedLink() throws Exception {
        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/inactive-link-info-request.json");

        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        ResultActions getByShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/short-link/" + linkInfoResponseCommonResponse.getBody().getShortLink()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private CommonResponse<LinkInfoResponse> createShortLink(String path) throws Exception {
        ResultActions createShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V_1_LINK_INFOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertFileToString(path)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String jsonResponse = createShortLinkResult.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<CommonResponse<LinkInfoResponse>>() {});
    }

    private CommonResponse<List<LinkInfoResponse>> findLinkByFilter() throws Exception {
        ResultActions findByFilterResult = mockMvc.perform(MockMvcRequestBuilders.get(API_V_1_LINK_INFOS))
                .andExpect(MockMvcResultMatchers.status().isOk());

        return objectMapper.readValue(
                findByFilterResult.andReturn().getResponse().getContentAsString(),
                new TypeReference<CommonResponse<List<LinkInfoResponse>>>() {});
    }

    private void updateLinkDescription(CommonResponse<List<LinkInfoResponse>> listCommonResponse) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(API_V_1_LINK_INFOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommonRequest<>(getUpdateShortLinkRequest(listCommonResponse)))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private UpdateShortLinkRequest getUpdateShortLinkRequest(CommonResponse<List<LinkInfoResponse>> listCommonResponse) throws IOException {
        UpdateShortLinkRequest updateShortLinkRequest = objectMapper
                .readValue(convertFileToString("json/request/update-link-info-request.json"), UpdateShortLinkRequest.class);
        updateShortLinkRequest.setId(listCommonResponse.getBody().getFirst().getId());
        return updateShortLinkRequest;
    }

    private void performLinkDeletion(String linkId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_V_1_LINK_INFOS + "/" + linkId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
