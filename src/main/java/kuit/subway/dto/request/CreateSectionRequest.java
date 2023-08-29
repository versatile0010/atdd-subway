package kuit.subway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSectionRequest {
    @NotNull(message = "새로운 구간의 하행역은 null 일 수 없습니다.")
    private Long downStationId;
    @NotNull(message = "새로운 구간의 상행역은 null 일 수 없습니다.")
    private Long upStationId;
    private Integer sectionType = 0;
    @NotNull(message = "새로운 구간의 거리 정보는 null 일 수 없습니다.")
    private Long distance;
}
