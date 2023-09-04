package kuit.subway.dto.response;

import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class DeleteStationResponse {
    private Long id;

    public static DeleteStationResponse from(Station station) {
        return DeleteStationResponse.builder()
                .id(station.getId())
                .build();
    }
}
