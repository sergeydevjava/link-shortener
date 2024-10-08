package org.sergeydevjava.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.service.LinkInfoService;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {

    public static final Integer LINK_LENGTH = 8;

    ConcurrentMap<String, CreateLinkInfoRequest> repository = new ConcurrentHashMap<>();

    public String createShortLink(CreateLinkInfoRequest createLinkInfoRequest) {
        String shortLink = RandomStringUtils.randomAlphanumeric(LINK_LENGTH);
        repository.put(shortLink, createLinkInfoRequest);
        return shortLink;
    }
}
