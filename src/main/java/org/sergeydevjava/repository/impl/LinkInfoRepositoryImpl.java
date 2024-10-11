package org.sergeydevjava.repository.impl;

import org.sergeydevjava.model.LinkInfo;
import org.sergeydevjava.repository.LinkInfoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    ConcurrentMap<String, LinkInfo> storage = new ConcurrentHashMap<>();

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        linkInfo.setId(UUID.randomUUID());
        storage.put(linkInfo.getShortLink(), linkInfo);
        return linkInfo;
    }

    @Override
    public Optional<LinkInfo> findByShortLink(String shortLink) {
        return Optional.ofNullable(storage.get(shortLink));
    }

    @Override
    public List<LinkInfo> findAll() {
        return storage.values().stream().toList();
    }
}
