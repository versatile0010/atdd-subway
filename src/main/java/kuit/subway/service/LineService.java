package kuit.subway.service;

import kuit.subway.domain.Line;
import kuit.subway.domain.Station;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.ModifyLineRequest;
import kuit.subway.dto.response.*;
import kuit.subway.exception.badrequest.DuplicatedLineNameException;
import kuit.subway.exception.notfound.NotFoundLineException;
import kuit.subway.exception.notfound.NotFoundStationException;
import kuit.subway.repository.LineRepository;
import kuit.subway.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public CreateLineResponse createOne(CreateLineRequest request) {
        validateStationId(request.getDownStationId());
        validateStationId(request.getUpStationId());
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

    private void validateDuplicatedName(String name) {
        if (lineRepository.existsByName(name)) {
            throw new DuplicatedLineNameException();
        }
    }

    private void validateStationId(Long id) {
        stationRepository.findById(id)
                .orElseThrow(NotFoundStationException::new);
    }

    public LineInfoResponse getLineDetails(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
        List<StationDto> stations =
                getStationPair(line.getDownStationId(), line.getUpStationId());
        return LineInfoResponse.from(line, stations);
    }

    @Transactional
    public ModifyLineResponse updateLine(ModifyLineRequest request, Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
        line.updateLine(request);
        return ModifyLineResponse.from(line.getId());
    }

    @Transactional
    public DeleteLineResponse deleteLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
        lineRepository.delete(line);
        return new DeleteLineResponse(id);
    }

    public List<StationDto> getStationPair(Long downStationId, Long upStationId) {
        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(NotFoundStationException::new);
        Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(NotFoundStationException::new);
        return List.of(StationDto.from(downStation), StationDto.from(upStation));
    }
}
