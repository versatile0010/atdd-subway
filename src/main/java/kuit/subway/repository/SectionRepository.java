package kuit.subway.repository;

import kuit.subway.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByDownStationIdAndUpStationId(Long downStationId, Long upStationId);
}
