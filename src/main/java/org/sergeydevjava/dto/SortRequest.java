package org.sergeydevjava.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    @NotEmpty(message = "Не задано поле для сортировки")
    private String field;
    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "Не определено напрвление сортировки")
    private String direction = "ASC";
}
