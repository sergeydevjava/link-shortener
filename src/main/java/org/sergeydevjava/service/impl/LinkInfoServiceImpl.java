package org.sergeydevjava.service.impl;


import com.sergeydevjava.annotation.LogExecutionTime;
import org.apache.commons.lang3.RandomStringUtils;
import org.sergeydevjava.dto.*;
import org.sergeydevjava.exception.NotFoundException;
import org.sergeydevjava.exception.NotFoundShortLinkException;
import org.sergeydevjava.mapper.LinkInfoMapper;
import org.sergeydevjava.model.LinkInfo;
import org.sergeydevjava.property.LinkInfoProperty;
import org.sergeydevjava.repository.LinkInfoRepository;
import org.sergeydevjava.service.LinkInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class LinkInfoServiceImpl implements LinkInfoService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final LinkInfoRepository linkInfoRepository;
    private final LinkInfoProperty linkInfoProperty;
    private final LinkInfoMapper linkInfoMapper;

    public LinkInfoServiceImpl(LinkInfoRepository linkInfoRepository, LinkInfoProperty linkInfoProperty, LinkInfoMapper linkInfoMapper) {
        this.linkInfoRepository = linkInfoRepository;
        this.linkInfoProperty = linkInfoProperty;
        this.linkInfoMapper = linkInfoMapper;
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest createLinkInfoRequest) {

        LinkInfo linkInfo = linkInfoMapper.fromCreateRequest(createLinkInfoRequest, RandomStringUtils.randomAlphabetic(linkInfoProperty.getShortLinkLength()));

        LinkInfo savedLinkInfo = linkInfoRepository.save(linkInfo);

        return linkInfoMapper.toResponse(savedLinkInfo);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse getByShortLink(String shortLink) {
        LinkInfo activeShortLink = linkInfoRepository.findActiveShortLink(shortLink, LocalDateTime.now())
                .orElseThrow(() -> new NotFoundShortLinkException("Не удалось найти активную не устаревшую сущность по короткой ссылке: " + shortLink));

        linkInfoRepository.incrementOpeningCountByShortLink(shortLink);

        return linkInfoMapper.toResponse(activeShortLink);

    }

    @Override
    @LogExecutionTime
    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest) {
        PageableRequest page = filterLinkInfoRequest.getPage();

        Pageable pageable = mapPageable(page);

        return linkInfoRepository.findByFilter(
                        filterLinkInfoRequest.getLinkPart(),
                        filterLinkInfoRequest.getEndTimeFrom(),
                        filterLinkInfoRequest.getEndTimeTo(),
                        filterLinkInfoRequest.getDescription(),
                        filterLinkInfoRequest.getActive(),
                        pageable
                )
                .stream()
                .map(linkInfoMapper::toResponse)
                .toList();
    }

    @Override
    @LogExecutionTime
    public void deleteById(UUID id) {
        linkInfoRepository.deleteById(id);
    }

    @Override
    @LogExecutionTime
    public LinkInfoResponse update(UpdateShortLinkRequest updateShortLinkRequest) {
        LinkInfo linkInfo = linkInfoRepository
                .findById(UUID.fromString(updateShortLinkRequest.getId()))
                .orElseThrow(() -> new NotFoundException("Не возможно найти сущность: идентификатор " + updateShortLinkRequest.getId()));

        if (isNotEmpty(updateShortLinkRequest.getLink())) {
            linkInfo.setLink(updateShortLinkRequest.getLink());
        }

        linkInfo.setEndTime(LocalDateTime.parse(updateShortLinkRequest.getEndTime(), formatter));

        if (isNotEmpty(updateShortLinkRequest.getDescription())) {
            linkInfo.setDescription(updateShortLinkRequest.getDescription());
        }
        if (!Objects.isNull(updateShortLinkRequest.getActive())) {
            linkInfo.setActive(updateShortLinkRequest.getActive());
        }
        return linkInfoMapper.toResponse(linkInfoRepository.save(linkInfo));
    }

    private static Pageable mapPageable(PageableRequest page) {
        List<Sort.Order> sorts = page.getSorts().stream()
                .map(sortRequest -> new Sort.Order(
                        Sort.Direction.valueOf(sortRequest.getDirection()),
                        sortRequest.getField()
                )).toList();
        return PageRequest.of(page.getNumber() - 1, page.getSize(), Sort.by(sorts));
    }

}
