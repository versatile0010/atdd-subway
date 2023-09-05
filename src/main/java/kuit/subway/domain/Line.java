package kuit.subway.domain;

import jakarta.persistence.*;
import kuit.subway.dto.request.ModifyLineRequest;
import kuit.subway.exception.badrequest.InvalidCreateLineException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "LINE")
public class Line extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    private Long distance;
    private String color;
    @Embedded
    private Sections sections = new Sections();

    public Line(String name, Long distance, String color) {
        this.name = name;
        this.distance = distance;
        this.color = color;
    }

    public void validateStations(Long downStationId, Long upStationId) {
        if (Objects.equals(downStationId, upStationId)) {
            throw new InvalidCreateLineException();
        }
    }

    public void updateLine(ModifyLineRequest request) {
        validateStations(request.getDownStationId(), request.getUpStationId());
        this.name = request.getName();
        this.distance = request.getDistance();
        this.color = request.getColor();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void removeSection(Long stationId) {
        sections.remove(stationId);
    }
    public void removeSectionV2(Section section) {
        sections.removeV2(section);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    private Line(Long id, String name, Long distance, String color) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.color = color;
    }

    public static Line createMock(Long id, String name, Long distance, String color) {
        return new Line(id, name, distance, color);
    }
}
