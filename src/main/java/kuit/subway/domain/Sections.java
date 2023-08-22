package kuit.subway.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kuit.subway.exception.badrequest.AlreadyExistStationException;
import kuit.subway.exception.badrequest.InvalidUpStationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        Line line = section.getLine();
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();

        validateUpStation(upStation.getId(), line.getDownStationId());
        validateDownStation(downStation.getId(), line);

        sections.add(section);
    }

    public List<Station> getDownStations() {
        return sections.stream().map(Section::getDownStation).toList();
    }

    public List<Station> getUpStations() {
        return sections.stream().map(Section::getUpStation).toList();
    }

    private void validateUpStation(Long newUpStationId, Long finalDownStationId) {
        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
        if (!Objects.equals(newUpStationId, finalDownStationId)) {
            throw new InvalidUpStationException();
        }
    }

    private void validateDownStation(Long newDownStationId, Line line) {
        // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        List<Station> downStations = getDownStations();
        validateExistStation(newDownStationId, downStations);
        List<Station> upStations = getUpStations();
        validateExistStation(newDownStationId, upStations);
    }

    private static void validateExistStation(Long newStationId, List<Station> stations) {
        for (Station upStation : stations) {
            if (Objects.equals(newStationId, upStation.getId())) {
                throw new AlreadyExistStationException();
            }
        }
    }
}
