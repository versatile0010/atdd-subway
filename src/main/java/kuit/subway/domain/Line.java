package kuit.subway.domain;

import jakarta.persistence.*;
import kuit.subway.exception.badrequest.InvalidCreateLineException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int distance;
    private Long downStationId;
    private Long upStationId;
    private String color;

    @Builder
    public Line(String name, int distance, Long downStationId, Long upStationId, String color) {
        validateStations(downStationId, upStationId);
        this.name = name;
        this.distance = distance;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.color = color;
    }

    public void validateStations(Long downStationId, Long upStationId) {
        if (downStationId == upStationId) {
            throw new InvalidCreateLineException();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }
}
