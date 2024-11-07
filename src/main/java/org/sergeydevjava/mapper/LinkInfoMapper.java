package org.sergeydevjava.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sergeydevjava.dto.CreateLinkInfoRequest;
import org.sergeydevjava.dto.LinkInfoResponse;
import org.sergeydevjava.model.LinkInfo;

@Mapper(componentModel = "spring")
public interface LinkInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingCount", constant = "0L")
    LinkInfo fromCreateRequest(CreateLinkInfoRequest request, String shortLink);

    LinkInfoResponse toResponse(LinkInfo linkInfo);
}
