package org.sergeydevjava.service;


import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.FilterLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;

import java.util.List;
import java.util.UUID;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateLinkInfoRequest createLinkInfoRequest);

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter(FilterLinkInfoRequest filterLinkInfoRequest);

    void deleteById(UUID id);

    LinkInfoResponse update(UpdateShortLinkRequest updateShortLinkRequest);
}
