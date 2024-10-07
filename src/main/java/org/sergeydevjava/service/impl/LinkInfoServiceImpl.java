package org.sergeydevjava.service.impl;

import lombok.RequiredArgsConstructor;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.service.LinkInfoService;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class LinkInfoServiceImpl implements LinkInfoService {

    private final Supplier<String> shortLinkGenerator;
    private final BiFunction<String, CreateLinkInfoRequest, CreateLinkInfoRequest> repository;

    public String createShortLink(CreateLinkInfoRequest createLinkInfoRequest) {
        String shortLink = shortLinkGenerator.get();
        repository.apply(shortLink, createLinkInfoRequest);
        return shortLink;
    }
}
