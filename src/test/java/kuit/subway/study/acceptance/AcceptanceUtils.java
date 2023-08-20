package kuit.subway.study.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

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
}
