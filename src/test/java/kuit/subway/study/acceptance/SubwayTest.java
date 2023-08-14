package kuit.subway.study.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.service.StationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.equalTo;

public class SubwayTest extends AcceptanceTest {
    @Autowired
    StationService stationService;

    @Test
    public void 지하철_역_생성() {
        // given
        CreateStationRequest request = new CreateStationRequest("강남역");
        // when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(request).contentType(ContentType.JSON)
                .when().post("/stations")
                .then().log().all()
                .extract();
        // then
        Assertions.assertEquals(200, extract.statusCode());
    }

    @Test
    public void 지하철_목록_조회() throws Exception {
        // given Given 2개의 지하철역을 생성하고
        stationService.createOne("강남역");
        stationService.createOne("서초역");

        // when 지하철 목록을 조회하면
        // then 2 개의 지하철 역 목록을 응답받는다.
        RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .assertThat().body("stations.size()", equalTo(2))
                .assertThat().statusCode(200);
    }

    @Test
    public void 지하철_삭제() throws Exception {
        // given 지하철 역을 생성하고
        stationService.createOne("강남역");

        // when 그 지하철 역을 삭제하면
        // then 그 지하철 역 목록 조회 시 생성한 역을 찾을 수 없다.
    }
}
