package kuit.subway.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "SECTION")
public class Section extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    private Long distance;

    private Section(Station downStation, Station upStation, Line line, Long distance) {
        this.downStation = downStation;
        this.upStation = upStation;
        this.line = line;
        this.distance = distance;
    }

    public static Section from(Station downStation, Station upStation, Line line, Long distance) {
        return new Section(downStation, upStation, line, distance);
    }

    private Section(Long id, Station downStation, Station upStation, Line line, Long distance) {
        this.id = id;
        this.downStation = downStation;
        this.upStation = upStation;
        this.line = line;
        this.distance = distance;
    }

    public static Section createMock(Long id, Station downStation, Station upStation, Line line, Long distance) {
        return new Section(id, downStation, upStation, line, distance);
    }
}
