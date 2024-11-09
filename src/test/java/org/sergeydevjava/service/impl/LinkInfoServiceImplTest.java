package org.sergeydevjava.service.impl;

import org.junit.jupiter.api.Test;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.FilterLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;
import org.sergeydevjava.exception.NotFoundException;
import org.sergeydevjava.property.LinkInfoProperty;
import org.sergeydevjava.service.LinkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LinkInfoServiceImplTest {


    private static final Integer NUMBER_OF_GENERATED_LINK = 10;
            ;
    @Autowired
    private LinkInfoProperty linkInfoProperty;

    @Autowired
    private LinkInfoService linkInfoService;

    @Test
    void createShortLinkTest() {
        LinkInfoResponse linkInfo = createShortLink();
        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfo.getShortLink().length());
    }

    @Test
    void getByShortLink() {
        LinkInfoResponse linkInfoCreationResponse = createShortLink();
        LinkInfoResponse linkInfoGetByShortLinkResponse = linkInfoService.getByShortLink(linkInfoCreationResponse.getShortLink());
        assertEquals(linkInfoCreationResponse, linkInfoGetByShortLinkResponse);
    }

    @Test
    void findAllShortLink() {
        IntStream.range(0, NUMBER_OF_GENERATED_LINK).forEach(it -> createShortLink());
        assertEquals(NUMBER_OF_GENERATED_LINK, linkInfoService.findByFilter(new FilterLinkInfoRequest()).size());
    }

    @Test
    void deleteById() {
        LinkInfoResponse linkInfo = createShortLink("http://somedomain1.com");
        assertNotNull(linkInfoService.getByShortLink(linkInfo.getShortLink()));
        linkInfoService.deleteById(linkInfo.getId());
        assertThrows(NotFoundException.class, () -> linkInfoService.getByShortLink(linkInfo.getShortLink()));
    }

    @Test
    void update() {
        LinkInfoResponse linkInfo = createShortLink("http://somedomain1.com");
        assertEquals(linkInfoProperty.getShortLinkLength(), linkInfo.getShortLink().length());
        assertTrue(linkInfoService.getByShortLink(linkInfo.getShortLink()).getActive());
        linkInfoService.update(UpdateShortLinkRequest.builder().id(linkInfo.getId().toString()).active(Boolean.FALSE).build());
        assertFalse(linkInfoService.getByShortLink(linkInfo.getShortLink()).getActive());
    }

    private LinkInfoResponse createShortLink() {
        return createShortLink(null);
    }

    private LinkInfoResponse createShortLink(String longLink) {
        CreateLinkInfoRequest createLinkInfoRequest = CreateLinkInfoRequest.builder()
                .link(isNotEmpty(longLink) ? longLink : "http://somedomain.com")
                .endTime(LocalDateTime.now().plusDays(30).toString())
                .description("Some description")
                .active(Boolean.TRUE)
                .build();

        return linkInfoService.createLinkInfo(createLinkInfoRequest);
    }

}