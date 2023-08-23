package kuit.subway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteSectionRequest {
    @NotNull(message = "삭제할 구간의 id 는 NULL 일 수 없습니다.")
    private Long sectionId;
}
