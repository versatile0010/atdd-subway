package kuit.subway.service.mock;

import kuit.subway.domain.Line;
import kuit.subway.domain.Section;
import kuit.subway.domain.Station;
import kuit.subway.dto.response.CreateLineResponse;
import kuit.subway.exception.badrequest.BadRequestException;
import kuit.subway.repository.LineRepository;
import kuit.subway.repository.SectionRepository;
import kuit.subway.repository.StationRepository;
import kuit.subway.service.LineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kuit.subway.study.acceptance.FixtureData.*;
import static org.mockito.Mockito.*;

@DisplayName("LineService Mock Test")
@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;
    @InjectMocks
    private LineService lineService;

    private Station 강남역;
    private Station 서초역;
    private Station 건대입구역;
    private Section 강남상행_건입하행구간;
    private Section 서초상행_강남하행구간;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = Station.createMock(1L, "강남역");
        서초역 = Station.createMock(2L, "서초역");
        건대입구역 = Station.createMock(3L, "건대입구역");
        이호선 = Line.createMock(1L, "이호선", 10, "green");
        강남상행_건입하행구간 = Section.createMock(1L, 건대입구역, 강남역, 이호선);
        서초상행_강남하행구간 = Section.createMock(1L, 강남역, 서초역, 이호선);
    }

    @DisplayName("강남역-서초역 이호선 노선 생성 Mock Test")
    @Test
    void createLine() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(서초역));
        when(lineRepository.save(any())).thenReturn(이호선);
        // when
        CreateLineResponse response = lineService.createOne(지하철_노선_생성_데이터_만들기("이호선", "green", 10, 1L, 2L));
        // then
        Assertions.assertEquals(1L, response.getId());
        verify(stationRepository, times(2)).findById(anyLong());
        verify(lineRepository, times(1)).save(any());
        verify(sectionRepository, times(1)).save(any());
    }


    @DisplayName("중복된 역으로 이호선 노선 생성 Mock Test")
    @Test
    void createWithDuplicatedStations() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        // when
        Assertions.assertThrows(BadRequestException.class,
                () -> lineService.createOne(지하철_노선_생성_데이터_만들기("이호선", "green", 10, 1L, 1L)));
        // then
        verify(stationRepository, times(2)).findById(anyLong());
        verify(lineRepository, times(0)).save(any());
        verify(sectionRepository, times(0)).save(any());
    }

    @DisplayName("구간 생성 Mock Test")
    @Test
    void addSection() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(서초역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(건대입구역));

        when(lineRepository.save(any())).thenReturn(createEmptyLine_이호선());
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));

        when(sectionRepository.save(any())).thenReturn(서초상행_강남하행구간);
        when(sectionRepository.save(any())).thenReturn(강남상행_건입하행구간);
        // when
        lineService.createOne(지하철_노선_생성_데이터_만들기("이호선", "green", 10, 1L, 2L));
        lineService.addSection(지하철_구간_생성_데이터_만들기(3L, 1L), 1L);
        // then
        verify(stationRepository, times(4)).findById(anyLong());
        verify(lineRepository, times(1)).save(any());
        verify(sectionRepository, times(2)).save(any());
    }
}
