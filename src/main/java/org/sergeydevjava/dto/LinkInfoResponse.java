package org.sergeydevjava.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LinkInfoResponse implements Comparable<LinkInfoResponse> {

    private UUID id;
    private String link;
    private String shortLink;
    private LocalDateTime endTime;
    private String description;
    private Boolean active;
    private Long openingCount;

    @Override
    public int compareTo(LinkInfoResponse linkInfoResponse) {
        return this.getLink().compareTo(linkInfoResponse.getLink());
    }
}
