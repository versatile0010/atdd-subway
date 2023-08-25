package kuit.subway.study.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.ModifyLineRequest;

import static kuit.subway.study.acceptance.AcceptanceUtils.*;

public class LineStep {
    private static final String LINE_PATH = "/lines";

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
