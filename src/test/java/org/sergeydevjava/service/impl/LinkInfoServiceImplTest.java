package org.sergeydevjava.service.impl;

import org.junit.jupiter.api.Test;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.repository.impl.LinkInfoRepositoryImpl;
import org.sergeydevjava.service.LinkInfoService;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.sergeydevjava.util.Constant.SHORT_LINK_LENGTH;

class LinkInfoServiceImplTest {

    public static final int NUMBER_OF_GENERATED_LINK = 10;

    @Test
    void createShortLink() {

        LinkInfoResponse linkInfo = createShortLink(new LinkInfoServiceImpl(new LinkInfoRepositoryImpl()));

        assertEquals(SHORT_LINK_LENGTH, linkInfo.getShortLink().length());
    }

    @Test
    void getByShortLink() {

        LinkInfoService linkInfoService = new LinkInfoServiceImpl(new LinkInfoRepositoryImpl());

        LinkInfoResponse linkInfoCreationResponse = createShortLink(linkInfoService);

        LinkInfoResponse linkInfoGetByShortLinkResponse = linkInfoService.getByShortLink(linkInfoCreationResponse.getShortLink());

        assertEquals(linkInfoCreationResponse, linkInfoGetByShortLinkResponse);
    }

    @Test
    void findAllShortLink() {

        LinkInfoService linkInfoService = new LinkInfoServiceImpl(new LinkInfoRepositoryImpl());

        IntStream.range(0, NUMBER_OF_GENERATED_LINK).forEach(it -> createShortLink(linkInfoService));

        assertEquals(NUMBER_OF_GENERATED_LINK, linkInfoService.findByFilter().size());
    }

    private static LinkInfoResponse createShortLink(LinkInfoService linkInfoService) {
        CreateLinkInfoRequest createLinkInfoRequest = CreateLinkInfoRequest.builder()
                .link("http://somedomain.com")
                .endTime(LocalDateTime.now().plusDays(30))
                .description("Some description")
                .active(Boolean.TRUE)
                .build();

        return linkInfoService.createLinkInfo(createLinkInfoRequest);
    }

}