package org.sergeydevjava.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.FilterLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;
import org.sergeydevjava.service.LinkInfoService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
public class LogExecutionTimeLinkInfoServiceProxy implements LinkInfoService {

    private final LinkInfoService linkInfoService;

    public LogExecutionTimeLinkInfoServiceProxy(LinkInfoService linkInfoService) {
        this.linkInfoService = linkInfoService;
    }

    @Override
    public LinkInfoResponse createLinkInfo(CreateLinkInfoRequest createLinkInfoRequest) {
        return supplyWithTimeTrack("createLinkInfo", () -> linkInfoService.createLinkInfo(createLinkInfoRequest));
    }

    @Override
    public LinkInfoResponse getByShortLink(String shortLink) {
        return supplyWithTimeTrack("getByShortLink", () -> linkInfoService.getByShortLink(shortLink));
    }

    @Override
    public List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest) {
        return supplyWithTimeTrack("findByFilter", () -> linkInfoService.findByFilter(filterLinkInfoRequest));
    }

    @Override
    public void deleteById(UUID id) {
        runWithTimeTrack("deleteById", () -> linkInfoService.deleteById(id));
    }

    @Override
    public LinkInfoResponse update(UpdateShortLinkRequest updateShortLinkRequest) {
        return supplyWithTimeTrack("update", () -> linkInfoService.update(updateShortLinkRequest));
    }

    private <T> T supplyWithTimeTrack(String methodName, Supplier<T> lambda) {
        Instant start = Instant.now();
        try {
            return lambda.get();
        } finally {
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toNanos();
            log.info("Время выполнения метода {}: {}", methodName, timeElapsed);
        }
    }

    private void runWithTimeTrack(String methodName, Runnable lambda) {
        Instant start = Instant.now();
        try {
            lambda.run();
        } finally {
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toNanos();
            log.info("Время выполнения метода {}: {}", methodName, timeElapsed);
        }
    }
}
