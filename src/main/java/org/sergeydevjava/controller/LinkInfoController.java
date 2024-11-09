package org.sergeydevjava.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.FilterLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.dto.UpdateShortLinkRequest;
import org.sergeydevjava.dto.common.CommonRequest;
import org.sergeydevjava.dto.common.CommonResponse;
import org.sergeydevjava.service.LinkInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/link-infos")
public class LinkInfoController {

    private final LinkInfoService linkInfoService;

    @PostMapping("/filter")
    public CommonResponse<List<LinkInfoResponse>> findByFilter(@RequestBody @Valid CommonRequest<FilterLinkInfoRequest> filterLinkInfoRequest) {
        List<LinkInfoResponse> foundByFilter = linkInfoService.findByFilter(filterLinkInfoRequest.getBody());
        return CommonResponse.<List<LinkInfoResponse>>builder()
                .body(foundByFilter)
                .build();
    }

    @PostMapping
    public CommonResponse<LinkInfoResponse> createLinkInfo(@RequestBody @Valid CommonRequest<CreateLinkInfoRequest> request) {
        LinkInfoResponse linkInfo = linkInfoService.createLinkInfo(request.getBody());
        return CommonResponse.<LinkInfoResponse>builder()
                .body(linkInfo)
                .build();
    }

    @PatchMapping
    public CommonResponse<LinkInfoResponse> updateLinkInfo(@RequestBody @Valid CommonRequest<UpdateShortLinkRequest> request) {
        LinkInfoResponse linkInfo = linkInfoService.update(request.getBody());
        return CommonResponse.<LinkInfoResponse>builder()
                .body(linkInfo)
                .build();
    }

    @DeleteMapping("/{linkId}")
    public CommonResponse<?> deleteLinkInfo(@PathVariable UUID linkId) {
        linkInfoService.deleteById(linkId);
        return CommonResponse.<LinkInfoResponse>builder()
                .build();
    }
}
