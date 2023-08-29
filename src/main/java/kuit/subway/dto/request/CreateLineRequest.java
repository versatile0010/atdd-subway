package kuit.subway.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateLineRequest {
    @NotNull(message = "지하철 노선의 색상은 Null 일 수 없습니다.")
    private String color;
    @NotNull(message = "지하철 노선의 거리는 Null 일 수 없습니다.")
    private Long distance;
    @Size(min = 3, max = 20)
    private String name;
    @NotNull(message = "지하철 노선의 하행 종점역은 정의되어야 합니다.")
    Long downStationId;
    @NotNull(message = "지하철 노선의 상행 종점역은 정의되어야 합니다.")
    Long upStationId;
}
