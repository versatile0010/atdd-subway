package kuit.subway.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kuit.subway.exception.badrequest.AlreadyExistStationException;
import kuit.subway.exception.badrequest.InvalidUpStationException;
import kuit.subway.exception.badrequest.NonFinalSectionRemoveException;
import kuit.subway.exception.badrequest.SingleSectionRemoveException;

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
        if (sections.size() != 0) {
            validateUpStation(upStation.getId(), line.getDownStationId());
            validateDownStation(downStation.getId(), line);
        }
        sections.add(section);
    }

    public void remove(Long stationId) {
        if (sections.size() <= 1) {
            // 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
            throw new SingleSectionRemoveException();
        }
        int lastSectionIdx = sections.size() - 1;
        Long finalDownStationId = sections.get(lastSectionIdx)
                .getDownStation()
                .getId();

        if (!Objects.equals(stationId, finalDownStationId)) {
            // 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
            throw new NonFinalSectionRemoveException();
        }
        sections.remove(lastSectionIdx);
    }

    public List<Station> getDownStations() {
        return sections
                .stream()
                .map(Section::getDownStation)
                .toList();
    }

    public List<Station> getUpStations() {
        return sections
                .stream()
                .map(Section::getUpStation)
                .toList();
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
