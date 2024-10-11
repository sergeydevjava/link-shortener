package org.sergeydevjava.service;


import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;

import java.util.List;

public interface LinkInfoService {

    LinkInfoResponse createLinkInfo(CreateLinkInfoRequest createLinkInfoRequest);

    LinkInfoResponse getByShortLink(String shortLink);

    List<LinkInfoResponse> findByFilter();


}
