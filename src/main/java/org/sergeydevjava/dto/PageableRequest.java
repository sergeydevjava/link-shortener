package org.sergeydevjava.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {

    @NotNull(message = "Номер страницы не задан")
    @Positive(message = "Номер страницы не может быть меньше 1")
    private Integer number;
    @NotNull(message = "Размер страницы не задан")
    @Positive(message = "Размер страницы не может быть меньше 1")
    private Integer size;

    @Valid
    @Builder.Default
    List<SortRequest> sorts = new ArrayList<>();

}
