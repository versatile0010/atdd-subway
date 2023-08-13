package kuit.subway.study.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateStationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubwayTest extends AcceptanceTest {
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
}
