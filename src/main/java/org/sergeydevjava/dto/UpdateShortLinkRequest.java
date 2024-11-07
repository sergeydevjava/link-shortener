package org.sergeydevjava.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.sergeydevjava.validation.ValidLocalDateTime;
import org.sergeydevjava.validation.ValidUUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShortLinkRequest {
    @ValidUUID
    private String id;
    @Pattern(regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$", message = "Url не соответствует патерну")
    private String link;
    @ValidLocalDateTime(shouldBeInFuture = false)
    private String endTime;
    private String description;
    private Boolean active;
}
