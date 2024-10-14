package org.sergeydevjava.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LinkInfoResponse {

    private UUID id;
    private String link;
    private String shortLink;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
    private Long openingCount;
}
