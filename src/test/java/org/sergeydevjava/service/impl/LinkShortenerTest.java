package org.sergeydevjava.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;
import org.sergeydevjava.dto.common.CommonRequest;
import org.sergeydevjava.dto.common.CommonResponse;
import org.sergeydevjava.dto.common.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LinkShortenerTest extends AbstractTest {

    public static final String API_V_1_LINK_INFOS = "/api/v1/link-infos";

    @BeforeEach
    void deleteExistingLinks() throws Exception {
        for (LinkInfoResponse lir : findLinkByFilter("json/request/find-all-filter-request.json").getBody()) {
            performLinkDeletion(lir.getId().toString());
        }
    }

    @Test
    void createLink() throws Exception {
        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request.json");

        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        assertEquals(0L, findLinkByFilter("json/request/find-by-link-part-request.json").getBody().get(0).getOpeningCount());

        ResultActions getByShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/short-link/" + linkInfoResponseCommonResponse.getBody().getShortLink()))
                .andExpect(MockMvcResultMatchers.status().isTemporaryRedirect());

        String link = parseHeader(getByShortLinkResult.andReturn(), HttpHeaders.LOCATION);

        assertEquals(linkInfoResponseCommonResponse.getBody().getLink(), link);

        assertEquals(1L, findLinkByFilter("json/request/find-by-link-part-request.json").getBody().get(0).getOpeningCount());
    }

    @Test
    void updateLink() throws Exception {

        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request.json");
        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        CommonResponse<List<LinkInfoResponse>> listCommonResponse = findLinkByFilter("json/request/find-all-filter-request.json");
        assertEquals(listCommonResponse.getBody().size(), 1L);
        assertEquals(listCommonResponse.getBody().get(0).getDescription(), "google");

        updateLinkDescription(listCommonResponse);

        CommonResponse<List<LinkInfoResponse>> listCommonResponseAfterUpdate = findLinkByFilter("json/request/find-all-filter-request.json");
        assertEquals(listCommonResponseAfterUpdate.getBody().size(), 1L);
        assertEquals(listCommonResponseAfterUpdate.getBody().get(0).getDescription(), "yandex");

    }

    @Test
    void deleteLink() throws Exception {

        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request.json");
        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfoResponseCommonResponse.getBody().getShortLink().length());

        CommonResponse<List<LinkInfoResponse>> listCommonResponse = findLinkByFilter("json/request/find-all-filter-request.json");
        assertEquals(listCommonResponse.getBody().size(), 1L);

        performLinkDeletion(listCommonResponse.getBody().get(0).getId().toString());

        CommonResponse<List<LinkInfoResponse>> listCommonResponseAfterUpdate = findLinkByFilter("json/request/find-all-filter-request.json");
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

    @Test
    void linkNotFoundPageShown() throws Exception {
        ResultActions getByShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/short-link/not-existing-short-link"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        assertEquals(convertFileToString("templates/404.html"), getByShortLinkResult.andReturn().getResponse().getContentAsString());
    }

    @Test
    void endTimeIsNotInFuture() throws Exception {
        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request-end-time-is-not-in-future.json", MockMvcResultMatchers.status().isBadRequest());
        assertEquals(1L, linkInfoResponseCommonResponse.getValidationErrors().size());
        assertEquals("body.endTime", linkInfoResponseCommonResponse.getValidationErrors().get(0).getField());
    }

    @Test
    void generalValidationTest() throws Exception {
        CommonResponse<LinkInfoResponse> linkInfoResponseCommonResponse = createShortLink("json/request/link-info-request-general-validation.json", MockMvcResultMatchers.status().isBadRequest());
        assertEquals(3L, linkInfoResponseCommonResponse.getValidationErrors().size());
        assertEquals(
                Set.of("body.description", "body.link", "body.active"),
                linkInfoResponseCommonResponse.getValidationErrors().stream()
                        .map(ValidationError::getField)
                        .collect(Collectors.toSet())
        );
    }

    @Test
    void testPagination() throws Exception {
        List<CommonResponse<LinkInfoResponse>> list = Stream.generate(() -> {
                    try {
                        return createShortLink("json/request/link-info-request.json");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .limit(3)
                .toList();

        assertEquals(1L, findLinkByFilter("json/request/find-by-link-part-request-pageable-number-1-size-1.json").getBody().size());
        assertEquals(3L, findLinkByFilter("json/request/find-by-link-part-request.json").getBody().size());
    }

    @Test
    void testPaginationAndSorting() throws Exception {

        createShortLink("json/request/link-info-request.json");
        createShortLink("json/request/link-info-request-yandex.json");
        createShortLink("json/request/link-info-request-rambler.json");


        assertEquals(1L, findLinkByFilter("json/request/find-by-link-part-request-pageable-number-1-size-1.json").getBody().size());
        assertEquals(3L, findLinkByFilter("json/request/find-by-link-part-request-pageable-number-1-size-3-sort-link-desc.json").getBody().size());
        assertEquals("yandex", findLinkByFilter("json/request/find-by-link-part-request-pageable-number-1-size-3-sort-link-desc.json").getBody().get(0).getDescription());
        assertEquals("google", findLinkByFilter("json/request/find-by-link-part-request-pageable-number-1-size-3-sort-link-asc.json").getBody().get(0).getDescription());
    }

    private CommonResponse<LinkInfoResponse> createShortLink(String path) throws Exception {
        return createShortLink(path, MockMvcResultMatchers.status().isOk());
    }


    private CommonResponse<LinkInfoResponse> createShortLink(String path, ResultMatcher resultMatcher) throws Exception {
        ResultActions createShortLinkResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V_1_LINK_INFOS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertFileToString(path)))
                .andExpect(resultMatcher);

        String jsonResponse = createShortLinkResult.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<CommonResponse<LinkInfoResponse>>() {
        });
    }

    private CommonResponse<List<LinkInfoResponse>> findLinkByFilter(String path) throws Exception {
        ResultActions findByFilterResult = mockMvc.perform(MockMvcRequestBuilders.post(API_V_1_LINK_INFOS + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertFileToString(path)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        return objectMapper.readValue(
                findByFilterResult.andReturn().getResponse().getContentAsString(),
                new TypeReference<CommonResponse<List<LinkInfoResponse>>>() {
                });
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
        updateShortLinkRequest.setId(listCommonResponse.getBody().get(0).getId().toString());
        return updateShortLinkRequest;
    }

    private void performLinkDeletion(String linkId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_V_1_LINK_INFOS + "/" + linkId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
