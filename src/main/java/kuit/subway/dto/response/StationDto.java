package kuit.subway.dto.response;

import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StationDto {
    private Long id;
    private String name;

    public StationDto(Station station) {
        this.id = station.getId();
        this.name = station.getName();
    }

    public static StationDto from(Station station) {
        return new StationDto(station);
    }
}
