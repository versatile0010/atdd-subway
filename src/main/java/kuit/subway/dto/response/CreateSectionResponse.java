package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class CreateSectionResponse {
    private Long id;
    private List<StationDto> stations;

    public static CreateSectionResponse of(Line line, List<Station> stations) {
        return CreateSectionResponse.builder()
                .id(line.getId())
                .stations(stations.stream().map(StationDto::from).toList())
                .build();
    }
}
