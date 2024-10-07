package org.sergeydevjava.service;


import org.sergeydevjava.dto.CreateLinkInfoRequest;

public interface LinkInfoService {

    String createShortLink(CreateLinkInfoRequest createLinkInfoRequest);

}
