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
            /* 현재 노선에 구간이 하나도 없으면 구간 추가에 대한 검증을 거치지 않음
               2단계 미션에서 요구사항이 추가되면 수정이 필요함. */
            validateUpStation(upStation.getId(), line.getDownStationId());
            validateDownStation(downStation.getId());
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
                .getId(); // 현재 하행종점역의 아이디

        if (!Objects.equals(stationId, finalDownStationId)) {
            // 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
            throw new NonFinalSectionRemoveException();
        }
        sections.remove(lastSectionIdx); // 가장 끝 구간을 삭제함. (현재 요구 사항에서는 마지막 구간만 제거 가능)
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

    private void validateDownStation(Long newDownStationId) {
        // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        List<Station> downStations = getDownStations();
        validateExistStation(newDownStationId, downStations);
        List<Station> upStations = getUpStations();
        validateExistStation(newDownStationId, upStations);
    }

    private static void validateExistStation(Long newStationId, List<Station> stations) {
        // stations 리스트에 newStationId 와 동일한 station 이 존재하면 예외 throw
        for (Station station : stations) {
            if (Objects.equals(newStationId, station.getId())) {
                throw new AlreadyExistStationException();
            }
        }
    }
}
