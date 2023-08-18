package kuit.subway.study.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateStationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kuit.subway.study.acceptance.AcceptanceFixture.*;
import static kuit.subway.study.acceptance.StationFixData.지하철_노선_생성_데이터;
import static kuit.subway.study.acceptance.StationFixData.지하철_역_생성_데이터;
import static org.hamcrest.Matchers.equalTo;

public class SubwayTest extends AcceptanceTest {
    private final int INVALID_INPUT_STATUS_CODE = 400;

    @Description("올바른 이름 요청 시 지하철 역이 정상적으로 생성되어야 한다.")
    @Test
    public void 지하철_역_생성_테스트() {
        // given
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        // when
        ExtractableResponse<Response> extract = 지하철_역_생성하기(강남역_데이터);
        // then
        Assertions.assertEquals(HttpStatus.OK.value(), extract.statusCode());
    }

    @Description("생성된 지하철 역 목록이 올바르게 조회되어야 한다.")
    @Test
    public void 지하철_목록_조회_테스트() {
        // given Given 2개의 지하철역을 생성하고
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터("서초역");

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
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        // when 그 지하철 역을 삭제하면
        Long 강남역_아이디 = 1L;
        지하철_역_삭제하기(강남역_아이디);
        // then 그 지하철 역 목록 조회 시 생성한 역을 찾을 수 없다.
        ExtractableResponse<Response> extract = 지하철_역_목록_조회하기();
        int 응답_코드 = extract.statusCode();
        Assertions.assertEquals(HttpStatus.OK.value(), 응답_코드);
    }

    @Description("올바르지 않은 이름으로 지하철 역 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 올바르지_않은_이름으로_지하철_역_생성_테스트() {
        // given 최소 길이보다 짧거나 최대 길이보다 긴 이름의 지하철 역 데이터로
        CreateStationRequest 최소길이보다_짧은_이름의_지하철역 = 지하철_역_생성_데이터("강역");
        CreateStationRequest 최대길이보다_긴_이름의_지하철역 = 지하철_역_생성_데이터("123456789.123456789.123456789");
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
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        // when "강남역" 이름으로 지하철 역을 생성하려고 하면
        ExtractableResponse<Response> extract = 지하철_역_생성하기(강남역_데이터);
        // then 거절되어야 하고, 지하철 역 목록에 반영되면 안된다.
        extract.response().then().log().all()
                .assertThat().statusCode(INVALID_INPUT_STATUS_CODE);
        지하철_역_목록_조회하기();
    }

    @Description("지하철 노선 요청이 올바르면, 노선이 생성되어야 한다.")
    @Test
    public void 지하철_노선_생성_테스트() {
        // given 2개의 지하철 역을 생성하고
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        Long 강남역_아이디 = 1L;

        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터("서초역");
        지하철_역_생성하기(서초역_데이터);
        Long 서초역_아이디 = 2L;
        // when 새로운 노선에 상행 종점역과 하행 종점역으로 요청을 보내면
        CreateLineRequest 이호선_데이터 = 지하철_노선_생성_데이터("2호선", "green", 10, 강남역_아이디, 서초역_아이디);
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(이호선_데이터);

        // then 생성된 노선의 Id 를 응답으로 받는다.
        extract.response().then().log().all()
                .assertThat().statusCode(HttpStatus.CREATED.value())
                .assertThat().body("id", equalTo(1));
    }

    @Description("동일한 지하철 역으로 노선 생성 시도 시, 거절되어야 한다.")
    @Test
    public void 동일_역에_대한_지하철_노선_생성_테스트() {
        // given 2개의 지하철 역을 생성하고
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        Long 강남역_아이디 = 1L;

        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터("서초역");
        지하철_역_생성하기(서초역_데이터);
        Long 서초역_아이디 = 1L;
        // when 상행 종점역과 하행 종점역을 같은 역으로 노선 생성 요청을 보내면
        CreateLineRequest 이호선_데이터 = 지하철_노선_생성_데이터("2호선", "green", 10, 강남역_아이디, 서초역_아이디);
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(이호선_데이터);

        // then 거절되어야 한다.
        extract.response().then().log().all()
                .assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Description("중복된 지하철 노선 이름으로 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 중복된_지하철_노선_생성_테스트() {
        // given 2개의 지하철 역을 생성하고 하나의 노선을 만든다.
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        Long 강남역_아이디 = 1L;

        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터("서초역");
        지하철_역_생성하기(서초역_데이터);
        Long 서초역_아이디 = 2L;

        CreateLineRequest 이호선_데이터 = 지하철_노선_생성_데이터("2호선", "green", 10, 강남역_아이디, 서초역_아이디);
        지하철_노선_생성하기(이호선_데이터);

        // when 동일한 이름의 노선으로 생성 요청을 보내면
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(이호선_데이터);
        // then 거절되어야 한다.
        extract.response().then().log().all()
                .assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Description("올바르지 않은 이름으로 노선 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 올바르지않은_이름으로_지하철_노선_생성_테스트() {
        // given 2개의 지하철 역을 생성하고 하나의 노선을 만든다.
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        Long 강남역_아이디 = 1L;

        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터("서초역");
        지하철_역_생성하기(서초역_데이터);
        Long 서초역_아이디 = 2L;

        CreateLineRequest 이호선_데이터 = 지하철_노선_생성_데이터("짧음", "green", 10, 강남역_아이디, 서초역_아이디);

        // when 동일한 이름의 노선으로 생성 요청을 보내면
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(이호선_데이터);
        // then 거절되어야 한다.
        extract.response().then().log().all()
                .assertThat().statusCode(INVALID_INPUT_STATUS_CODE);
    }

    @Description("올바른 요청으로 노선을 조회한 경우, 노선 정보가 올바르게 조회되어야 한다.")
    @Test
    public void 지하철_노선_조회_테스트() {
        // given 2 개의 지하철 역으로 이루어진 하나의 노선을 생성하고
        CreateStationRequest 강남역_데이터 = 지하철_역_생성_데이터("강남역");
        지하철_역_생성하기(강남역_데이터);
        Long 강남역_아이디 = 1L;

        CreateStationRequest 서초역_데이터 = 지하철_역_생성_데이터("서초역");
        지하철_역_생성하기(서초역_데이터);
        Long 서초역_아이디 = 2L;

        CreateLineRequest 이호선_데이터 = 지하철_노선_생성_데이터("짧음", "green", 10, 강남역_아이디, 서초역_아이디);
        지하철_노선_생성하기(이호선_데이터);
        // when 해당 지하철 노선을 조회하면
        Long 이호선_아이디 = 1L;
        ExtractableResponse<Response> extract = 지하철_노선_조회하기(이호선_아이디);
        // then 잘 조회되어야 한다.
        extract.response().then().log().all()
                .assertThat().statusCode(HttpStatus.OK.value());
    }

    @Description("존재하지 않는 노선을 조회하려고 하는 경우, 거절되어야 한다.")
    @Test
    public void 존재하지_않는_지하철_노선_조회_테스트() {
        // given & when 존재하지 않는 노선을 조회하려 하면
        Long 노선_아이디 = 1L;
        ExtractableResponse<Response> extract = 지하철_노선_조회하기(노선_아이디);
        // then 거절되어야 한다.
        extract.response().then().log().all()
                .assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
