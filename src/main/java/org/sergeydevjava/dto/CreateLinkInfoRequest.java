package org.sergeydevjava.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLinkInfoRequest {
    private String link;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
}
