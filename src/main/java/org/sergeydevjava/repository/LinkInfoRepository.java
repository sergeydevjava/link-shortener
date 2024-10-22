package org.sergeydevjava.repository;

import org.sergeydevjava.model.LinkInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository {
    LinkInfo save(LinkInfo linkInfo);

    Optional<LinkInfo> findByShortLink(String shortLink);

    List<LinkInfo> findAll();

    void deleteById(UUID id);

    Optional<LinkInfo> findById(UUID id);
}
