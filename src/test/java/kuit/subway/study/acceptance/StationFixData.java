package kuit.subway.study.acceptance;

import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.request.ModifyLineRequest;

import static kuit.subway.study.acceptance.AcceptanceFixture.지하철_노선_생성하기;
import static kuit.subway.study.acceptance.AcceptanceFixture.지하철_역_생성하기;

public class StationFixData {
    public static CreateStationRequest 지하철_역_생성_데이터_만들기(String name) {
        return new CreateStationRequest(name);
    }

    public static CreateLineRequest 지하철_노선_생성_데이터_만들기(String name, String color, int distance, Long downStationId, Long upStationId) {
        return new CreateLineRequest(color, distance, name, downStationId, upStationId);
    }

    public static ModifyLineRequest 지하철_노선_수정_데이터_만들기(String name, String color, int distance, Long downStationId, Long upStationId) {
        return new ModifyLineRequest(color, distance, name, downStationId, upStationId);
    }

    public static Long 이호선_노선_더미데이터_생성하기(Long downStationId, Long upStationId) {
        CreateLineRequest 이호선 = 지하철_노선_생성_데이터_만들기("이호선", "green", 10, downStationId, upStationId);
        지하철_노선_생성하기(이호선);
        return 1L;
    }

    public static void 강남역_서초역_더미데이터_생성하기() {
        CreateStationRequest 강남역 = 지하철_역_생성_데이터_만들기("강남역");
        CreateStationRequest 서초역 = 지하철_역_생성_데이터_만들기("서초역");
        지하철_역_생성하기(강남역);
        지하철_역_생성하기(서초역);
    }
}
