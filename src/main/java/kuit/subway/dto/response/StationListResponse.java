package kuit.subway.dto.response;

import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class StationListResponse {
    private List<StationDto> stations;
    public static StationListResponse from(List<Station> stations) {
        return StationListResponse.builder()
                .stations(stations.stream().map(StationDto::from).toList())
                .build();
    }
}
