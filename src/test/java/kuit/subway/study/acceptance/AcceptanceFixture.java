package kuit.subway.study.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateStationRequest;

public class AcceptanceFixture {
    private static final String STATION_PATH = "/stations";
    private static final String LINE_PATH = "/lines";

    public static ExtractableResponse<Response> 지하철_역_생성하기(CreateStationRequest request) {
        return RestAssured.given().log().all()
                .body(request).contentType(ContentType.JSON)
                .when().post(STATION_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회하기() {
        return RestAssured.given().log().all()
                .when().get(STATION_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_역_삭제하기(Long 지하철_역_아이디) {
        return RestAssured.given().log().all()
                .when().delete(STATION_PATH + "/{id}", 지하철_역_아이디)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 지하철_노선_생성하기(CreateLineRequest request) {
        return RestAssured.given().log().all()
                .body(request).contentType(ContentType.JSON)
                .when().post(LINE_PATH)
                .then().log().all()
                .extract();
    }
}
