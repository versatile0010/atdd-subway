package kuit.subway.study.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.dto.request.CreateSectionRequest;
import kuit.subway.dto.request.DeleteSectionRequest;
import kuit.subway.dto.request.DeleteSectionRequestV2;

import static kuit.subway.study.acceptance.AcceptanceUtils.post;
import static kuit.subway.study.acceptance.AcceptanceUtils.delete;

public class SectionStep {
    private static final String LINE_PATH = "/lines";
    private static final String SECTION_PATH = "/sections";

    public static ExtractableResponse<Response> 지하철_구간_생성하기(CreateSectionRequest body, Long id) {
        return post(LINE_PATH + "/" + id + SECTION_PATH, body);
    }
    public static ExtractableResponse<Response> 지하철_구간_삭제하기(DeleteSectionRequest body, Long id) {
        return delete(LINE_PATH + "/" + id + SECTION_PATH, body);
    }

    public static ExtractableResponse<Response> 지하철_구간_삭제하기V2(DeleteSectionRequestV2 body, Long id) {
        return delete(LINE_PATH + "/" + id + SECTION_PATH, body);
    }
}
