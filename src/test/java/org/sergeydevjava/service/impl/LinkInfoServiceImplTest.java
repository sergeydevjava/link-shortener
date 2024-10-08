package org.sergeydevjava.service.impl;

import org.junit.jupiter.api.Test;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.service.LinkInfoService;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.sergeydevjava.service.impl.LinkInfoServiceImpl.LINK_LENGTH;

class LinkInfoServiceImplTest {

    @Test
    void linkInfoServiceImplTest() {

        LinkInfoService linkInfoService = new LinkInfoServiceImpl();

        CreateLinkInfoRequest createLinkInfoRequest = CreateLinkInfoRequest.builder()
                .link("http://somedomain.com")
                .endTime(LocalDateTime.now().plusDays(30))
                .description("Some description")
                .active(Boolean.TRUE)
                .build();

        String shortLink = linkInfoService.createShortLink(createLinkInfoRequest);

        System.out.println(shortLink);

        assertEquals(LINK_LENGTH, shortLink.length());
    }

}