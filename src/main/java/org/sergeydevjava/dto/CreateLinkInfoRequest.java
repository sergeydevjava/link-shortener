package org.sergeydevjava.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.sergeydevjava.validation.ValidLocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateLinkInfoRequest {
    @NotEmpty(message = "Ссылка не модет быть пустой")
    @Pattern(regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$", message = "Url не соответствует патерну")
    private String link;
    @ValidLocalDateTime(shouldBeInFuture = true, message = "Дата должна быть валидной и относиться к будущему вреени")
    private String endTime;
    @NotEmpty(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Признак активности (active) не может быть null")
    private Boolean active;
}
