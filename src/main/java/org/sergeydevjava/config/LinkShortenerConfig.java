package org.sergeydevjava.config;

import org.sergeydevjava.property.LinkInfoProperty;
import org.sergeydevjava.repository.LinkInfoRepository;
import org.sergeydevjava.repository.impl.LinkInfoRepositoryImpl;
import org.sergeydevjava.service.LinkInfoService;
import org.sergeydevjava.service.impl.LinkInfoServiceImpl;
import org.sergeydevjava.service.impl.LogExecutionTimeLinkInfoServiceProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkShortenerConfig {

    @Bean
    public LinkInfoRepository linkInfoRepository() {
        return new LinkInfoRepositoryImpl();
    }

    @Bean
    public LinkInfoService linkInfoService(LinkInfoRepository linkInfoRepository, LinkInfoProperty linkInfoProperty) {
        LinkInfoServiceImpl linkInfoService = new LinkInfoServiceImpl(linkInfoRepository, linkInfoProperty);
        return new LogExecutionTimeLinkInfoServiceProxy(linkInfoService);
    }
}
