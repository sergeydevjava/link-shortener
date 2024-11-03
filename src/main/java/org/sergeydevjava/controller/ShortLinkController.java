package org.sergeydevjava.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sergeydevjava.service.LinkInfoService;
import org.sergeydevjava.validation.ValidLink;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/short-link")
@RequiredArgsConstructor
public class ShortLinkController {

    private final LinkInfoService linkInfoService;

    @GetMapping("/{shortLink}")
    public ResponseEntity<String> getByShortLink(@Valid @ValidLink @PathVariable String shortLink) {
        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .header(HttpHeaders.LOCATION, linkInfoService.getByShortLink(shortLink).getLink())
                .build();
    }
}
