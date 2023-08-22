package kuit.subway.study.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.equalTo;

public class AcceptanceUtils {
    public static ExtractableResponse<Response> get(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(String path, Long id) {
        return RestAssured.given().log().all()
                .when().get(path + "/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object body, Long id) {
        return RestAssured.given().log().all()
                .body(body).contentType(ContentType.JSON)
                .when().post(path + "/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> post(String path, Object body) {
        return RestAssured.given().log().all()
                .body(body).contentType(ContentType.JSON)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(String path, Long id) {
        return RestAssured.given().log().all()
                .when().delete(path + "/{id}", id)
                .then().log().all()
                .extract();
    }

    public static void 응답결과_검증하기(ExtractableResponse<Response> extract, HttpStatus expectedStatus) {
        Assertions.assertEquals(expectedStatus.value(), extract.statusCode());
    }

    public static void 응답결과_검증하기(ExtractableResponse<Response> extract, int code) {
        Assertions.assertEquals(code, extract.statusCode());
    }

    public static void 응답바디값_기대값과_같은지_검증하기(ExtractableResponse<Response> extract, String path, int expected) {
        extract.response().then().assertThat().body(path, equalTo(expected));
    }
}
