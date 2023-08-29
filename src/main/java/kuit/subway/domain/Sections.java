package kuit.subway.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import kuit.subway.exception.badrequest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();
    private final int ZERO = 0;
    private final int HEAD = 0;
    private final int SECTION_AT_BETWEEN = 2;
    private final int SECTION_AT_FIRST = 1;
    private final int SECTION_AT_LAST = 0;

    public void add(Section section, int sectionType) {
        if (sections.size() == 0) {
            sections.add(section);
            return;
        }
        List<Station> stations = getStations();
        validateAddSection(stations, section);
        switch (sectionType) {
            case SECTION_AT_BETWEEN -> addSectionAtBetween(section);
            case SECTION_AT_FIRST -> addSectionAtFirst(section);
            case SECTION_AT_LAST -> addSectionAtLast(section);
        }
    }

    private void addSectionAtFirst(Section section) {
        Station downStation = section.getDownStation();
        Section firstSection = sections.get(0);
        Station upStationOfFirstSection = firstSection.getUpStation();
        // firstSection 의 Up 이랑 section 의 down 이랑 다르면 exception
        if (!upStationOfFirstSection.equals(downStation)) {
            throw new InvalidAddFirstSectionException();
        }
        sections.add(HEAD, section);
    }

    private void addSectionAtLast(Section section) {
        Station upStation = section.getUpStation();
        final int LAST = sections.size() - 1;
        Section lastSection = sections.get(LAST);
        Station downStationOfLastSection = lastSection.getDownStation();
        // lastSection.down 이랑 section 의 up 이 다르면 exception
        if (!downStationOfLastSection.equals(upStation)) {
            throw new InvalidAddLastSectionException();
        }
        sections.add(LAST + 1, section);
    }

    private void addSectionAtBetween(Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        Line line = section.getLine();
        for (int i = 0; i < sections.size(); i++) {
            Section curSection = sections.get(i);
            Station curDownStation = curSection.getDownStation();
            Station curUpStation = curSection.getUpStation();
            for (Station station : new Station[]{curUpStation, curDownStation}) {
                if (station.equals(upStation)) {
                    validateAddSectionDistance(curSection, section);
                    Long diff = curSection.getDistance() - section.getDistance();
                    sections.add(i, section);
                    sections.set(i + 1, Section.from(curDownStation, upStation, line, diff));
                    return;
                }
            }
        }
    }

    private void validateAddSectionDistance(Section section, Section insertedSection) {
        // section 에 insertedSection 을 사이에 끼어넣는 상황
        if (section.getDistance() <= insertedSection.getDistance()) {
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

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        for (int i = ZERO; i < sections.size(); i++) {
            Section cur = sections.get(i);
            if (i == ZERO) {
                stations.add(cur.getUpStation());
                stations.add(cur.getDownStation());
                continue;
            }
            stations.add(cur.getDownStation());
        }
        return stations;
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
