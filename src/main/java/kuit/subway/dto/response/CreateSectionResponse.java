package kuit.subway.dto.response;

import kuit.subway.domain.Line;
import kuit.subway.domain.Station;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSectionResponse {
    private Long id;
    private List<StationDto> stations;

    private CreateSectionResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.stations = stations
                .stream()
                .map(StationDto::from)
                .toList();
    }
    public static CreateSectionResponse of(Line line, List<Station> stations) {
        return new CreateSectionResponse(line, stations);
    }
}
