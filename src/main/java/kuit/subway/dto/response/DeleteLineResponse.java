package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class DeleteLineResponse {
    private Long id;

    public static DeleteLineResponse from(Line line) {
        return DeleteLineResponse.builder()
                .id(line.getId())
                .build();
    }
}
