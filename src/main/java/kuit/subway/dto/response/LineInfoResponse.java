package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class LineInfoResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationDto> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static LineInfoResponse of(Line line, List<Station> stations) {
        return LineInfoResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .createdDate(line.getCreatedDate())
                .modifiedDate(line.getModifiedDate())
                .stations(stations.stream().map(StationDto::from).toList())
                .build();
    }
}
