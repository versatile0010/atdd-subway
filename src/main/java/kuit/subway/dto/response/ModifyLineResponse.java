package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ModifyLineResponse {
    private Long id;

    private ModifyLineResponse(Line line) {
        this.id = line.getId();
    }

    public static ModifyLineResponse from(Line line) {
        return new ModifyLineResponse(line);
    }
}
