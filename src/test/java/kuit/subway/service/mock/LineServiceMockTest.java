package kuit.subway.service.mock;

import kuit.subway.domain.Line;
import kuit.subway.domain.Section;
import kuit.subway.domain.Station;
import kuit.subway.dto.request.CreateSectionRequest;
import kuit.subway.dto.request.DeleteSectionRequest;
import kuit.subway.dto.request.ModifyLineRequest;
import kuit.subway.dto.response.*;
import kuit.subway.exception.badrequest.BadRequestException;
import kuit.subway.exception.badrequest.InvalidAddSectionDistanceException;
import kuit.subway.exception.badrequest.SingleSectionRemoveException;
import kuit.subway.exception.notfound.NotFoundLineException;
import kuit.subway.repository.LineRepository;
import kuit.subway.repository.SectionRepository;
import kuit.subway.repository.StationRepository;
import kuit.subway.service.LineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kuit.subway.exception.CustomExceptionContext.INVALID_ADD_SECTION_DISTANCE_ERROR;
import static kuit.subway.study.acceptance.FixtureData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
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
    private Station 성수역;
    private Section 강남상행_건입하행구간;
    private Section 서초상행_강남하행구간;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = Station.createMock(1L, "강남역");
        서초역 = Station.createMock(2L, "서초역");
        건대입구역 = Station.createMock(3L, "건대입구역");
        성수역 = Station.createMock(4L, "성수역");
        이호선 = Line.createMock(1L, "이호선", 10L, "green");
        서초상행_강남하행구간 = Section.createMock(1L, 강남역, 서초역, 이호선, 2L);
        강남상행_건입하행구간 = Section.createMock(2L, 건대입구역, 강남역, 이호선, 4L);
    }

    @DisplayName("강남역과 서초역을 구간으로 가지는 이호선을 만들 수 있다.")
    @Test
    void createLine() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(서초역));
        when(lineRepository.save(any())).thenReturn(이호선);
        // when
        CreateLineResponse response = lineService.createOne(지하철_노선_생성_데이터_만들기("이호선", "green", 10L, 1L, 2L));
        // then
        assertEquals(1L, response.getId());
        verify(stationRepository, times(2)).findById(anyLong());
        verify(lineRepository, times(1)).save(any());
        verify(sectionRepository, times(1)).save(any());
    }


    @DisplayName("중복된 역으로만 이루어진 구간으로 지하철 노선을 생성할 수 없다.")
    @Test
    void createWithDuplicatedStations() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        // when
        assertThrows(BadRequestException.class,
                () -> lineService.createOne(지하철_노선_생성_데이터_만들기("이호선", "green", 10L, 1L, 1L)));
        // then
        verify(stationRepository, times(2)).findById(anyLong());
        verify(lineRepository, times(0)).save(any());
        verify(sectionRepository, times(0)).save(any());
    }

    @DisplayName("line-id 으로 지하철 노선 정보를 조회할 수 있다.")
    @Test
    void addLine() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        // when
        LineInfoResponse response = lineService.getLineDetails(1L);
        // then
        assertAll(
                () -> assertEquals(2, response.getStations().size()),
                () -> assertEquals(1, response.getId()),
                () -> assertEquals("이호선", response.getName()),
                () -> assertEquals(1, response.getId()),
                () -> assertThrows(NotFoundLineException.class, () -> lineService.getLineDetails(2L)) // 예외 케이스
        );
    }

    @DisplayName("지하철 노선을 수정할 수 있다.")
    @Test
    void updateLine() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        // when
        ModifyLineRequest request = 지하철_노선_수정_데이터_만들기("삼호선", "orange", 99L, 3L, 4L);
        ModifyLineResponse response = lineService.updateLine(request, 1L);
        // then
        assertAll(
                () -> assertEquals(1L, response.getId()),
                () -> assertThrows(NotFoundLineException.class, () -> lineService.updateLine(request, 9L)) // 예외 케이스
        );
        verify(lineRepository, times(1)).findById(1L);
    }

    @DisplayName("지하철 노선을 삭제할 수 있다.")
    @Test
    void deleteLine() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        // when
        DeleteLineResponse response = lineService.deleteLine(1L);
        // then
        assertAll(
                () -> assertEquals(1L, response.getId()),
                () -> assertThrows(NotFoundLineException.class, () -> lineService.deleteLine(9L)) // 예외 케이스
        );
        verify(lineRepository, times(1)).delete(이호선);
        verify(sectionRepository, times(0)).delete(any());
    }

    @DisplayName("단일 구간을 가지는 노선에 대해서는 구간을 제거할 수 없다.")
    @Test
    void removeSectionAtSingleSections() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(서초상행_강남하행구간));
        // when
        DeleteSectionRequest request = 지하철_구간_삭제_데이터_만들기(1L);
        // then
        assertThrows(SingleSectionRemoveException.class, () -> lineService.removeSection(request, 1L));
    }

    @DisplayName("두개 이상의 구간을 가지는 노선에 대해서 마지막 구간 제거가 가능하다. ")
    @Test
    void removeSectionAtMultipleSections() {
        // given
        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        when(sectionRepository.findById(2L)).thenReturn(Optional.of(강남상행_건입하행구간));

        이호선.addSection(서초상행_강남하행구간);
        이호선.addSection(강남상행_건입하행구간);
        // when
        DeleteSectionRequest request = 지하철_구간_삭제_데이터_만들기(2L);
        DeleteSectionResponse response = lineService.removeSection(request, 1L);
        // then
        assertAll(
                () -> assertEquals(2L, response.getId())
        );
    }

    @DisplayName("상행 종점 구간을 추가할 수 있다.")
    @Test
    void addSectionAtFirst() {
        // given  (서초역-강남역) 노선에 (건대입구역-서초역) 구간을 상행종점 구간으로 등록할 수 있다.
        when(stationRepository.findById(2L)).thenReturn(Optional.of(서초역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(건대입구역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(2L, 3L, 10L);
        CreateSectionResponse response = lineService.addSection(request, 1L);
        // then
        assertThat(response.getStations()).hasSize(3)
                .extracting("name")
                .containsExactly("건대입구역", "서초역", "강남역");
    }

    @DisplayName("기존에 존재하는 노선 사이에 새로운 구간을 추가할 수 있다.")
    @Test
    void addSectionAtBetweenCase1() {
        // given (서초--2->강남--4->건대 입구) 노선에 (강남--1->성수) 구간을 등록할 수 있다.
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(성수역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        이호선.addSection(강남상행_건입하행구간);

        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(4L, 1L, 1L);
        CreateSectionResponse response = lineService.addSection(request, 1L);

        // then
        assertThat(response.getStations()).hasSize(4)
                .extracting("name")
                .containsExactly("서초역", "강남역", "성수역", "건대입구역");
    }

    @DisplayName("기존 구간보다 거리가 크거나 같은 구간은 추가할 수 없다.")
    @Test
    void addSectionAtBetweenCase2() {
        // given (서초--2->강남--4->건대 입구) 노선에 (강남--4->성수) 구간을 등록할 수 없다.
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(성수역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        이호선.addSection(강남상행_건입하행구간);
        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(4L, 1L, 4L);
        // then
        assertThatThrownBy(() -> lineService.addSection(request, 1L))
                .isInstanceOf(InvalidAddSectionDistanceException.class)
                .extracting("code")
                .isEqualTo(INVALID_ADD_SECTION_DISTANCE_ERROR.getCode());
    }

    @DisplayName("하행 종점 구간을 추가할 수 있다.")
    @Test
    void addSectionAtBetweenCase3() {
        // given (서초--2->강남--4->건대 입구) 노선에 (건대 입구역 -> 성수역) 구간을 등록할 수 있다.
        when(stationRepository.findById(3L)).thenReturn(Optional.of(건대입구역));
        when(stationRepository.findById(4L)).thenReturn(Optional.of(성수역));

        when(lineRepository.findById(1L)).thenReturn(Optional.of(이호선));
        이호선.addSection(서초상행_강남하행구간);
        이호선.addSection(강남상행_건입하행구간);

        // when
        CreateSectionRequest request = 지하철_구간_생성_데이터_만들기(4L, 3L, 1L);
        CreateSectionResponse response = lineService.addSection(request, 1L);

        // then
        assertThat(response.getStations()).hasSize(4)
                .extracting("name")
                .containsExactly("서초역", "강남역", "건대입구역", "성수역");
    }
}
