package org.sergeydevjava.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.service.LinkInfoService;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class LinkInfoServiceImplTest {

    public static final int LINK_LENGTH = 8;
    private static final int NUM_THREADS = 100;

    @Test
    void linkInfoServiceImplTest() {

        ConcurrentHashMap<String, CreateLinkInfoRequest> repository = new ConcurrentHashMap<>();
        Supplier<String> shortLinkGenerator = () -> RandomStringUtils.randomAlphanumeric(LINK_LENGTH);

        LinkInfoService linkInfoService = new LinkInfoServiceImpl(shortLinkGenerator, repository::put);

        CreateLinkInfoRequest createLinkInfoRequest = CreateLinkInfoRequest.builder()
                .link("http://somedomain.com")
                .endTime(LocalDateTime.now().plusDays(30))
                .description("Some description")
                .active(Boolean.TRUE)
                .build();

        String shortLink = linkInfoService.createShortLink(createLinkInfoRequest);

        System.out.println(shortLink);

        assertEquals(shortLink, repository.keySet().iterator().next());
        assertEquals(1, repository.keySet().size());

    }

    @Test
    void linkInfoServiceImplTestSeveralThreads() throws InterruptedException {
        ConcurrentHashMap<String, CreateLinkInfoRequest> repository = new ConcurrentHashMap<>();
        Supplier<String> shortLinkGenerator = () -> RandomStringUtils.randomAlphanumeric(LINK_LENGTH);

        LinkInfoService linkInfoService = new LinkInfoServiceImpl(shortLinkGenerator, repository::put);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            CreateLinkInfoRequest request = CreateLinkInfoRequest.builder()
                    .link("http://somedomain.com/link" + i)
                    .endTime(LocalDateTime.now().plusDays(30))
                    .description("Some description " + i)
                    .active(true)
                    .build();

            executor.execute(() -> linkInfoService.createShortLink(request));
        }

        executor.shutdown();
        boolean finishedBeforeTimeout = executor.awaitTermination(1, TimeUnit.MINUTES);

        assertTrue(finishedBeforeTimeout);

        assertEquals(NUM_THREADS, repository.size());
    }

}