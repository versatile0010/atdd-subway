package kuit.subway.service;

import kuit.subway.domain.Line;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.response.CreateLineResponse;
import kuit.subway.exception.badrequest.DuplicatedLineNameException;
import kuit.subway.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    @Transactional
    public CreateLineResponse createOne(CreateLineRequest request) {
        validateStations(request.getDownStationId(), request.getUpStationId());
        validateDuplicatedName(request.getName());
        Long savedLineId = lineRepository.save(Line.builder()
                .color(request.getColor())
                .distance(request.getDistance())
                .name(request.getName())
                .downStationId(request.getDownStationId())
                .upStationId(request.getUpStationId())
                .build()).getId();
        return new CreateLineResponse(savedLineId);
    }

    public void validateDuplicatedName(String name){
        if(lineRepository.existsByName(name)){
            throw new DuplicatedLineNameException();
        }
    }
    public void validateStations(Long downStationId, Long upStationId){
        stationService.validateInvalidStationId(downStationId);
        stationService.validateInvalidStationId(upStationId);
    }
}
