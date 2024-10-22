package org.sergeydevjava.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;
import org.sergeydevjava.exception.NotFoundException;
import org.sergeydevjava.model.LinkInfo;
import org.sergeydevjava.property.LinkInfoProperty;
import org.sergeydevjava.repository.LinkInfoRepository;
import org.sergeydevjava.service.LinkInfoService;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


public class LinkInfoServiceImpl implements LinkInfoService {

    private final LinkInfoRepository linkInfoRepository;
    private final LinkInfoProperty linkInfoProperty;

    public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository, LinkInfoProperty linkInfoProperty) {
        this.linkInfoRepository = linkInfoRepository;
        this.linkInfoProperty = linkInfoProperty;
    }

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest createLinkInfoRequest) {
        LinkInfo linkInfo = LinkInfo.builder()
                .link(createLinkInfoRequest.getLink())
                .shortLink(RandomStringUtils.randomAlphabetic(linkInfoProperty.getShortLinkLength()))
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

    @Override
    public void deleteById(UUID id) {
        linkInfoRepository.deleteById(id);
    }

    @Override
    public LinkInfoResponse update(UpdateShortLinkRequest updateShortLinkRequest) {
        LinkInfo linkInfo = linkInfoRepository
                .findById(updateShortLinkRequest.getId())
                .orElseThrow(() -> new NotFoundException("Не возможно найти сущность: идентификатор " + updateShortLinkRequest.getId()));
        if (isNotEmpty(updateShortLinkRequest.getLink())) {
            linkInfo.setLink(updateShortLinkRequest.getLink());
        }
        if (nonNull(updateShortLinkRequest.getEndTime())) {
            linkInfo.setEndTime(updateShortLinkRequest.getEndTime());
        }
        if (isNotEmpty(updateShortLinkRequest.getDescription())) {
            linkInfo.setDescription(updateShortLinkRequest.getDescription());
        }
        if (nonNull(updateShortLinkRequest.getActive())) {
            linkInfo.setActive(updateShortLinkRequest.getActive());
        }
        return toResponse(linkInfoRepository.save(linkInfo));
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
