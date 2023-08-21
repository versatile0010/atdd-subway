package kuit.subway.repository;

import kuit.subway.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {
    boolean existsByName(String name);
}
