package kuit.subway.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "STATION")
public class Station extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Builder
    public Station(String name) {
        this.name = name;
    }

    private Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station createMock(Long id, String name) {
        return new Station(id, name);
    }
    @Override
    public boolean equals(Object o) {
        Station station = (Station) o;
        return Objects.equals(id, station.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.intValue() : 0;
    }
}
