package kuit.subway.study.acceptance;

import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.request.ModifyLineRequest;

public class StationFixData {
    public static CreateStationRequest 지하철_역_생성_데이터(String name) {
        return new CreateStationRequest(name);
    }

    public static CreateLineRequest 지하철_노선_생성_데이터(String name, String color, int distance, Long downStationId, Long upStationId) {
        return new CreateLineRequest(color, distance, name, downStationId, upStationId);
    }

    public static ModifyLineRequest 지하철_노선_수정_데이터(String name, String color, int distance, Long downStationId, Long upStationId) {
        return new ModifyLineRequest(color, distance, name, downStationId, upStationId);
    }
}
