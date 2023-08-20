package kuit.subway.study.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.request.ModifyLineRequest;

import static kuit.subway.study.acceptance.AcceptanceUtils.*;

public class AcceptanceFixture {
    private static final String STATION_PATH = "/stations";
    private static final String LINE_PATH = "/lines";

    public static ExtractableResponse<Response> 지하철_역_생성하기(CreateStationRequest body) {
        return post(STATION_PATH, body);
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회하기() {
        return get(STATION_PATH);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제하기(Long id) {
        return delete(STATION_PATH, id);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성하기(CreateLineRequest body) {
        return post(LINE_PATH, body);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회하기(Long id) {
        return get(LINE_PATH, id);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정하기(ModifyLineRequest body, Long id) {
        return post(LINE_PATH, body, id);
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제하기(Long 지하철_노선_아이디) {
        return delete(LINE_PATH, 지하철_노선_아이디);
    }
}
