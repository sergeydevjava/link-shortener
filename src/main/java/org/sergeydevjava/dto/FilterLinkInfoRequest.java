package org.sergeydevjava.dto;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterLinkInfoRequest {

    private String linkPart;
    private LocalDateTime endTimeFrom;
    private LocalDateTime endTimeTo;
    private String description;
    private Boolean active;

    @Valid
    @Builder.Default
    private PageableRequest page = new PageableRequest(1, 5, List.of());
}
