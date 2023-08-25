package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CreateLineResponse {
    private Long id;

    private CreateLineResponse(Line line) {
        this.id = line.getId();
    }

    public static CreateLineResponse from(Line line) {
        return new CreateLineResponse(line);
    }
}
