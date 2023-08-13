package kuit.subway.service;

import kuit.subway.domain.Station;
import kuit.subway.dto.response.CreateStationResponse;
import kuit.subway.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public CreateStationResponse createOne(String name) {
        Station savedStation = stationRepository.save(
                Station.builder()
                        .name(name)
                        .build());
        return new CreateStationResponse(savedStation.getId());
    }
}
