package kuit.subway.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ModifyLineResponse {
    private Long id;

    private ModifyLineResponse(Long id) {
        this.id = id;
    }

    public static ModifyLineResponse from(Long id) {
        return new ModifyLineResponse(id);
    }
}
