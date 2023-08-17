package kuit.subway.study.acceptance;

import kuit.subway.dto.request.CreateStationRequest;

public class StationFixData {
    public static CreateStationRequest 지하철_역_생성_데이터(String name){
        return new CreateStationRequest(name);
    }
}
