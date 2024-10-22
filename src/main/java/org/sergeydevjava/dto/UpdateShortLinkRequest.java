package org.sergeydevjava.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShortLinkRequest {

    private UUID id;
    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}
