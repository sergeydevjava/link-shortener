package org.sergeydevjava.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.exception.NotFoundException;
import org.sergeydevjava.model.LinkInfo;
import org.sergeydevjava.repository.LinkInfoRepository;
import org.sergeydevjava.service.LinkInfoService;

import java.util.List;

import static org.sergeydevjava.util.Constant.SHORT_LINK_LENGTH;


public class LinkInfoServiceImpl implements LinkInfoService {

    private final LinkInfoRepository linkInfoRepository;

    public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository) {
        this.linkInfoRepository = linkInfoRepository;
    }

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest createLinkInfoRequest) {
        LinkInfo linkInfo = LinkInfo.builder()
                .link(createLinkInfoRequest.getLink())
                .shortLink(RandomStringUtils.randomAlphabetic(SHORT_LINK_LENGTH))
                .endTime(createLinkInfoRequest.getEndTime())
                .description(createLinkInfoRequest.getDescription())
                .active(createLinkInfoRequest.getActive())
                .openingCount(0L)
                .build();

        LinkInfo savedLinkInfo = linkInfoRepository.save(linkInfo);

        return toResponse(savedLinkInfo);
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        return linkInfoRepository.findByShortLink(shortLink)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Не удалось найти сущность по короткой ссылке: " + shortLink));
    }

    @Override
    public List<LinkInfoResponse> findByFilter() {
        return linkInfoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LinkInfoResponse toResponse(LinkInfo linkInfo) {
        return LinkInfoResponse.builder()
                .id(linkInfo.getId())
                .link(linkInfo.getLink())
                .shortLink(linkInfo.getShortLink())
                .endTime(linkInfo.getEndTime())
                .description(linkInfo.getDescription())
                .active(linkInfo.getActive())
                .openingCount(linkInfo.getOpeningCount())
                .build();
    }
}
