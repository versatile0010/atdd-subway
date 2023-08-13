package kuit.subway.study.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.AcceptanceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class SubwayTest extends AcceptanceTest {
    @Test
    public void 지하철_역_생성() {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("name", "강남역");
        // when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .body(body)
                .when().post("stations")
                .then().log().all()
                .extract();
        // then
        Assertions.assertEquals(200, extract.statusCode());
    }
}
