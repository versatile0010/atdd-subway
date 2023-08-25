package kuit.subway.study.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.dto.request.CreateStationRequest;

import static kuit.subway.study.acceptance.AcceptanceUtils.*;

public class StationStep {
    private static final String STATION_PATH = "/stations";

    public static ExtractableResponse<Response> 지하철_역_생성하기(CreateStationRequest body) {
        return post(STATION_PATH, body);
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회하기() {
        return get(STATION_PATH);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제하기(Long id) {
        return delete(STATION_PATH, id);
    }
}
