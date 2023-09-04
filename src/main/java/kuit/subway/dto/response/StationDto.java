package kuit.subway.dto.response;

import kuit.subway.domain.Station;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
public class StationDto {
    private Long id;
    private String name;

    public static StationDto from(Station station) {
        return StationDto.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }
}
