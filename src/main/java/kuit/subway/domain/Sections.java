package kuit.subway.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kuit.subway.exception.badrequest.*;
import kuit.subway.exception.notfound.NotFoundSectionException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();
    private final int HEAD = 0;

    public void add(Section section) {
        if (sections.isEmpty()) { // 최초로 삽입되는 구간인경우
            sections.add(section);
            return;
        }
        List<Station> stations = getStations(); // 1. 현재 지하철 노선의 정렬된 역 목록을 가져와서
        validateAddSection(stations, section); // 2. 해당 section 을 추가할 수 있는 지 검증
        if (isSectionLast(section, stations)) { // case A. 가장 끝에 추가되어야 하는 경우
            addSectionAtLast(section);
            return;
        }
        if (isSectionFirst(section, stations)) { // case B. 가장 앞에 추가되어야 하는 경우
            addSectionAtFirst(section);
            return;
        }
        addSectionAtBetween(section); // case C. 사이에 끼워넣어져야 하는 경우
    }

    private boolean isSectionLast(Section section, List<Station> stations) {
        Station upStation = section.getUpStation();
        Station finalStation = stations.get(stations.size() - 1);
        return finalStation.equals(upStation); // 삽입할 구간의 상행역과 하행 종점역이 같으면 마지막에 추가되어야 하는 CASE
    }

    private boolean isSectionFirst(Section section, List<Station> stations) {
        Station downStation = section.getDownStation();
        Station firstStation = stations.get(HEAD);
        return firstStation.equals(downStation); // 삽입할 구간의 하행역과 상행 종점역이 같으면 맨 처음에 추가되어야 하는 CASE
    }

    private void addSectionAtFirst(Section section) {
        Station downStation = section.getDownStation(); // 추가할 구간의 하행역과
        Section firstSection = sections.get(HEAD); // 가장 처음 구간을 가져온다.
        Station upStationOfFirstSection = firstSection.getUpStation();
        // 첫 구간의 상행역과 추가할 구간의 하행역이 다르면 추가할 수 없다.
        if (!upStationOfFirstSection.equals(downStation)) {
            throw new InvalidAddFirstSectionException();
        }
        sections.add(HEAD, section); // 그 외의 경우에는 추가할 수 있다.
    }

    private void addSectionAtLast(Section section) {
        Station upStation = section.getUpStation(); // 추가할 구간의 상행역과
        int tail = sections.size() - 1;
        Section lastSection = sections.get(tail); // 가장 마지막 구간을 가져온다
        Station downStationOfLastSection = lastSection.getDownStation(); // 하행 종점역을 가져온다.
        // 하행 종점역과 추가할 구간의 상행역이 다르면 추가할 수 없다.
        if (!downStationOfLastSection.equals(upStation)) {
            throw new InvalidAddLastSectionException();
        }
        // 외의 경우에는 추가한다.
        sections.add(tail + 1, section);
    }

    private void addSectionAtBetween(Section newSection) {
        // 추가할 구간의 상행역을, 상행역으로 가지고 있는 가장 처음 구간(let oldSection)을 가져온다.
        Section oldSection = findFirstSectionByUpStation(newSection.getUpStation()).orElseThrow(NotFoundSectionException::new);
        // 기존 구간보다 크거나 같은 거리의 새로운 구간을 사이에 추가할 수 없다.
        validateAddSectionDistance(oldSection, newSection);
        // 검증을 통과한다면 추가한다.
        int indexOfOldSection = IntStream.range(0, sections.size()) // oldSection 의 순서를 찾아낸다.
                .filter(index -> sections.get(index)
                        .equals(oldSection))
                .boxed().findFirst().orElseThrow(NotFoundSectionException::new);
        sections.add(indexOfOldSection, newSection); // oldSection 자리에 새로운 구간을 추가하고 oldSection 을 적절히 수정한다.
        oldSection.updateUpStation(newSection.getDownStation(), oldSection.getDistance() - newSection.getDistance());
    }

    private Optional<Section> findFirstSectionByUpStation(Station station) {
        // station 을 상행역으로 가지는 구간 중 가장 상행인 구간을 반환한다.
        return sections.stream()
                .filter(section -> section.getUpStation().equals(station))
                .findFirst();
    }

    private Optional<Section> findFirstSectionByDownStation(Station station) {
        // station 을 하행역으로 가지는 구간 중 가장 상행인 구간을 반환한다.
        return sections.stream()
                .filter(section -> section.getDownStation().equals(station))
                .findFirst();
    }

    public void showStations() {
        // 디버깅 용도로 지하철 노선 역 목록을 출력하는 함수이다.
        StringBuilder sb = new StringBuilder("지하철 노선 목록 출력: (상행 종점)");
        for (Station station : getStations()) {
            sb.append(station.getName()).append("->");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append("(하행 종점)");
        log.info(sb.toString());
    }

    private void validateAddSectionDistance(Section section, Section insertedSection) {
        // section 에 insertedSection 을 사이에 끼워넣는 상황
        if (section.getDistance() <= insertedSection.getDistance()) {
            // 기존 구간보다 크거나 같은 거리의 새로운 구간을 사이에 추가할 수 없다.
            throw new InvalidAddSectionDistanceException();
        }
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


    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Section firstSection = sections.get(HEAD);
        Station upStation = firstSection.getUpStation();

        stations.add(upStation); // 상행 종점역은 리스트에 바로 추가한다.
        Optional<Section> section = findFirstSectionByUpStation(upStation);
        // 첫 번째 구간의 상행역을 상행역으로 가지는 구간을 가져온다.
        // 최초에는 firstSection 이 얻어진다.
        updateStationsRecursive(stations, section); // 재귀적으로 탐색하며 리스트에 추가한다.

        return stations;
    }

    private void updateStationsRecursive(List<Station> stations, Optional<Section> section) {
        if (section.isEmpty()) {
            return; // terminate condition 은 더이상 탐색할 section 이 존재하지 않는 경우이다.
        }
        Station downStation = section.get().getDownStation(); // 해당 구간의 하행역을 가져와서
        stations.add(downStation); // 리스트에 추가한 뒤
        // 해당 구간의 하행역을 상행역으로 가지는 다음 구간을 재귀적으로 탐색한다.
        updateStationsRecursive(stations, findFirstSectionByUpStation(downStation));
    }

    private void validateAddSection(List<Station> stations, Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        boolean isUpStationExists = false;
        boolean isDownStationExists = false;
        for (Station station : stations) {
            if (station.equals(upStation)) {
                isUpStationExists = true;
            }
            if (station.equals(downStation)) {
                isDownStationExists = true;
            }
            if (isUpStationExists && isDownStationExists) {
                throw new AlreadyExistStationsException();
            }
        }
        if (!isUpStationExists && !isDownStationExists) {
            throw new InvalidAddSectionsException();
        }
    }
}
