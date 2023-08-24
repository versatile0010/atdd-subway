package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import kuit.subway.domain.Station;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class LineInfoResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationDto> stations;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long finalDownStationId;
    private Long finalUpStationId;

    private LineInfoResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.createdDate = line.getCreatedDate();
        this.modifiedDate = line.getModifiedDate();
        this.stations = stations
                .stream()
                .map(StationDto::from)
                .toList();
        this.finalDownStationId = line.getDownStationId();
        this.finalUpStationId = line.getUpStationId();
    }

    public static LineInfoResponse from(Line line, List<Station> stations) {
        return new LineInfoResponse(line, stations);
    }
}
