package org.sergeydevjava.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(builderClassName = "CommonResponseBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private UUID id;
    private T body;

    public static <T> CommonResponseBuilder<T> builder() {
        return new CommonResponseBuilderWithIdGeneration<>();
    }

    private static class CommonResponseBuilderWithIdGeneration<T> extends CommonResponseBuilder<T> {
        @Override
        public CommonResponse<T> build() {
            CommonResponse<T> commonResponse = super.build();
            commonResponse.setId(UUID.randomUUID());
            return commonResponse;
        }
    }
}
