package kuit.subway.service;

import kuit.subway.domain.Line;
import kuit.subway.domain.Section;
import kuit.subway.domain.Station;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateSectionRequest;
import kuit.subway.dto.request.DeleteSectionRequest;
import kuit.subway.dto.request.ModifyLineRequest;
import kuit.subway.dto.response.*;
import kuit.subway.exception.badrequest.DuplicatedLineNameException;
import kuit.subway.exception.notfound.NotFoundLineException;
import kuit.subway.exception.notfound.NotFoundSectionException;
import kuit.subway.exception.notfound.NotFoundStationException;
import kuit.subway.repository.LineRepository;
import kuit.subway.repository.SectionRepository;
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
    private final SectionRepository sectionRepository;

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

    @Transactional
    public CreateSectionResponse addSection(CreateSectionRequest request, Long id) {
        Long downStationId = request.getDownStationId();
        Long upStationId = request.getUpStationId();

        Line line = lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(NotFoundStationException::new);
        Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(NotFoundStationException::new);

        Section section = Section.from(downStation, upStation, line);
        line.addSection(section); // 해당 노선에 대하여 구간 추가 및 검증
        line.setDownStationId(downStationId); // 해당 노선의 하행종점을 새로운 구간의 하행역으로 변경

        sectionRepository.save(section);
        return new CreateSectionResponse(section.getId());
    }
    @Transactional
    public DeleteSectionResponse deleteSection(DeleteSectionRequest request, Long id){
        // request body 로 section id 를 받고, path parameter 으로 line id 를 받는다.
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(NotFoundSectionException::new);
        Line line = lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
        Station downStation = section.getDownStation();

        line.removeSection(downStation.getId()); // section 제거
        return new DeleteSectionResponse(request.getSectionId()); // 제거한 section id 를 반환
    }
}
