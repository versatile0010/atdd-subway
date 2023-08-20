package kuit.subway.study.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateStationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kuit.subway.study.acceptance.AcceptanceFixture.*;
import static kuit.subway.study.acceptance.StationFixData.지하철_역_생성_데이터_만들기;
import static org.hamcrest.Matchers.equalTo;

public class StationAcceptanceTest extends AcceptanceTest {
    private final int INVALID_INPUT_STATUS_CODE = 400;

    @Description("올바른 이름 요청 시 지하철 역이 정상적으로 생성되어야 한다.")
    @Test
    public void 지하철_역_생성_테스트() {
        // given
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터_만들기("강남역");
        // when
        ExtractableResponse<Response> extract = 지하철_역_생성하기(강남역_데이터);
        // then
        Assertions.assertEquals(HttpStatus.OK.value(), extract.statusCode());
    }

    @Description("생성된 지하철 역 목록이 올바르게 조회되어야 한다.")
    @Test
    public void 지하철_목록_조회_테스트() {
        // given Given 2개의 지하철역을 생성하고
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터_만들기("강남역");
        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터_만들기("서초역");

        지하철_역_생성하기(강남역_데이터);
        지하철_역_생성하기(서초역_데이터);
        // when 지하철 목록을 조회하면
        // then 2 개의 지하철 역 목록을 응답받는다.
        ExtractableResponse<Response> extract = 지하철_역_목록_조회하기();
        ValidatableResponse 검증가능한_응답 = extract.response().then().log().all();
        검증가능한_응답
                .assertThat().body("stations.size()", equalTo(2))
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @Description("생성된 지하철 역에 대한 삭제 요청 시, 해당 지하철 역은 목록에서 제거되어야 한다.")
    @Test
    public void 지하철_삭제_테스트() {
        // given 지하철 역을 생성하고
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터_만들기("강남역");
        지하철_역_생성하기(강남역_데이터);
        // when 그 지하철 역을 삭제하면
        Long 강남역_아이디 = 1L;
        ExtractableResponse<Response> extract1 = 지하철_역_삭제하기(강남역_아이디);
        extract1.response().then().log().all()
                .assertThat().statusCode(HttpStatus.NO_CONTENT.value());
        // then 그 지하철 역 목록 조회 시 생성한 역을 찾을 수 없다.
        ExtractableResponse<Response> extract2 = 지하철_역_목록_조회하기();
        int 응답_코드 = extract2.statusCode();
        Assertions.assertEquals(HttpStatus.OK.value(), 응답_코드);
    }

    @Description("올바르지 않은 이름으로 지하철 역 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 올바르지_않은_이름으로_지하철_역_생성_테스트() {
        // given 최소 길이보다 짧거나 최대 길이보다 긴 이름의 지하철 역 데이터로
        CreateStationRequest 최소길이보다_짧은_이름의_지하철역 = 지하철_역_생성_데이터_만들기("강역");
        CreateStationRequest 최대길이보다_긴_이름의_지하철역 = 지하철_역_생성_데이터_만들기("123456789.123456789.123456789");
        // when 지하철 역 생성 요청 시
        ExtractableResponse<Response> extract1 = 지하철_역_생성하기(최소길이보다_짧은_이름의_지하철역);
        ExtractableResponse<Response> extract2 = 지하철_역_생성하기(최대길이보다_긴_이름의_지하철역);
        // then 거절되어야 하고, 지하철 역 목록에 반영되면 안된다.
        extract1.response().then().log().all()
                .assertThat().statusCode(INVALID_INPUT_STATUS_CODE);
        extract2.response().then().log().all()
                .assertThat().statusCode(INVALID_INPUT_STATUS_CODE);
        지하철_역_목록_조회하기();
    }

    @Description("이미 존재하는 이름으로 지하철 역 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 이미존재하는_이름으로_지하철역_생성_테스트() {
        // given "강남역" 지하철역이 이미 존재할 때
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터_만들기("강남역");
        지하철_역_생성하기(강남역_데이터);
        // when "강남역" 이름으로 지하철 역을 생성하려고 하면
        ExtractableResponse<Response> extract = 지하철_역_생성하기(강남역_데이터);
        // then 거절되어야 하고, 지하철 역 목록에 반영되면 안된다.
        extract.response().then().log().all()
                .assertThat().statusCode(INVALID_INPUT_STATUS_CODE);
        지하철_역_목록_조회하기();
    }
}
