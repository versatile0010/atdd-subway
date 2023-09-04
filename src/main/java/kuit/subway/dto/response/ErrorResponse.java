package kuit.subway.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private int code;
    private String message;

    public static ErrorResponse from(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
