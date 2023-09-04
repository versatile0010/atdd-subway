package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class ModifyLineResponse {
    private Long id;

    public static ModifyLineResponse from(Line line) {
        return ModifyLineResponse.builder()
                .id(line.getId())
                .build();
    }
}
