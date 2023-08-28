package kuit.subway.study.acceptance;

import kuit.subway.domain.Line;
import kuit.subway.domain.Station;
import kuit.subway.dto.request.*;

import static kuit.subway.study.acceptance.line.LineStep.지하철_노선_생성하기;
import static kuit.subway.study.acceptance.station.StationStep.지하철_역_생성하기;

public class FixtureData {
    public static CreateStationRequest 지하철_역_생성_데이터_만들기(String name) {
        return new CreateStationRequest(name);
    }

    public static CreateLineRequest 지하철_노선_생성_데이터_만들기(String name, String color, int distance, Long downStationId, Long upStationId) {
        return new CreateLineRequest(color, distance, name, downStationId, upStationId);
    }

    public static ModifyLineRequest 지하철_노선_수정_데이터_만들기(String name, String color, int distance, Long downStationId, Long upStationId) {
        return new ModifyLineRequest(color, distance, name, downStationId, upStationId);
    }

    public static CreateSectionRequest 지하철_구간_생성_데이터_만들기(Long downStationId, Long upStationId, int sectionType) {
        return new CreateSectionRequest(downStationId, upStationId, sectionType);
    }

    public static DeleteSectionRequest 지하철_구간_삭제_데이터_만들기(Long sectionId) {
        return new DeleteSectionRequest(sectionId);
    }

    public static void 강남역_서초역_생성하기() {
        CreateStationRequest 강남역 = 지하철_역_생성_데이터_만들기("강남역");
        CreateStationRequest 서초역 = 지하철_역_생성_데이터_만들기("서초역");
        지하철_역_생성하기(강남역);
        지하철_역_생성하기(서초역);
    }

    public static void 이호선_강남역하행_서초역상행_생성하기() {
        CreateStationRequest 강남역 = 지하철_역_생성_데이터_만들기("강남역");
        CreateStationRequest 서초역 = 지하철_역_생성_데이터_만들기("서초역");
        지하철_역_생성하기(강남역);
        지하철_역_생성하기(서초역);
        CreateLineRequest 이호선 = new CreateLineRequest("green", 10, "2호선", 1L, 2L);
        지하철_노선_생성하기(이호선);
    }

    public static Station create_강남역() {
        return Station.createMock(1L, "강남역");
    }

    public static Station create_서초역() {
        return Station.createMock(2L, "서초역");
    }

    public static Line createEmptyLine_이호선() {
        return Line.createMock(1L, "이호선", 10, "green");
    }
}
