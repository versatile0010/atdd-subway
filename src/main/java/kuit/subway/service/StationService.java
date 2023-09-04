package kuit.subway.service;

import kuit.subway.domain.Station;
import kuit.subway.dto.response.CreateStationResponse;
import kuit.subway.dto.response.DeleteStationResponse;
import kuit.subway.dto.response.StationDto;
import kuit.subway.dto.response.StationListResponse;
import kuit.subway.exception.badrequest.DuplicatedStationNameException;
import kuit.subway.exception.notfound.NotFoundStationException;
import kuit.subway.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public CreateStationResponse createOne(String name) {
        validateDuplicatedName(name);
        Station savedStation = stationRepository.save(
                Station.builder()
                        .name(name)
                        .build());
        return CreateStationResponse.of(savedStation);
    }

    public StationListResponse getStations() {
        List<Station> savedStations = stationRepository.findAll();
        return StationListResponse.from(savedStations);
    }

    @Transactional
    public DeleteStationResponse deleteStation(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(NotFoundStationException::new);
        stationRepository.delete(station);
        return DeleteStationResponse.from(station);
    }

    private void validateDuplicatedName(String name) {
        if (stationRepository.existsByName(name))
            throw new DuplicatedStationNameException();
    }
}
