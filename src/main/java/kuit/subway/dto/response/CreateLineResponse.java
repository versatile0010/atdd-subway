package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CreateLineResponse {
    private Long id;

    public static CreateLineResponse from(Line line) {
        return CreateLineResponse.builder()
                .id(line.getId())
                .build();
    }
}
