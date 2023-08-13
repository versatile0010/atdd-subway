package kuit.subway.study;

import io.restassured.RestAssured;
import kuit.subway.AcceptanceTest;
import org.junit.jupiter.api.Test;

public class RestTest extends AcceptanceTest {

    @Test
    void restTest() {
        RestAssured.given().log().all()
                .when().get("https://google.com")
                .then().log().all()
                .extract();
    }
}
