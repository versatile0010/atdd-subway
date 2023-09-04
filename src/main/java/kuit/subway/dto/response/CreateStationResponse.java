package kuit.subway.dto.response;

import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CreateStationResponse {
    private Long id;

    public static CreateStationResponse of(Station station) {
        return CreateStationResponse.builder()
                .id(station.getId())
                .build();
    }
}
