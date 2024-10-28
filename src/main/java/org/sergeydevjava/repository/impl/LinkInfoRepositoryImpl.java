package org.sergeydevjava.repository.impl;

import org.sergeydevjava.model.LinkInfo;
import org.sergeydevjava.repository.LinkInfoRepository;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Objects.isNull;

@Service()
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkInfoRepositoryImpl implements LinkInfoRepository {

    private ConcurrentMap<String, LinkInfo> storage = new ConcurrentHashMap<>();

    @Override
    public LinkInfo save(LinkInfo linkInfo) {
        if (isNull(linkInfo.getId())) {
            linkInfo.setId(UUID.randomUUID());
        }
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

    @Override
    public void deleteById(UUID id) {
        storage.entrySet().removeIf(stringLinkInfoEntry -> stringLinkInfoEntry.getValue().getId().equals(id));
    }

    @Override
    public Optional<LinkInfo> findById(UUID id) {
        return storage.values().stream()
                .filter(linkInfo -> linkInfo.getId().equals(id))
                .findFirst();
    }
}
