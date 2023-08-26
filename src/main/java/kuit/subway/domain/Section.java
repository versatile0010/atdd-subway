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

    private Section(Station downStation, Station upStation, Line line) {
        this.downStation = downStation;
        this.upStation = upStation;
        this.line = line;
    }

    public static Section from(Station downStation, Station upStation, Line line) {
        return new Section(downStation, upStation, line);
    }

    private Section(Long id, Station downStation, Station upStation, Line line) {
        this.id = id;
        this.downStation = downStation;
        this.upStation = upStation;
        this.line = line;
    }
    public static Section createMock(Long id, Station downStation, Station upStation, Line line) {
        return new Section(id, downStation, upStation, line);
    }
}
