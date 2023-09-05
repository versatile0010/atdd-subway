package kuit.subway.study.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateSectionRequest;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.request.DeleteSectionRequest;
import kuit.subway.dto.request.DeleteSectionRequestV2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kuit.subway.study.acceptance.AcceptanceUtils.응답결과_검증하기;
import static kuit.subway.study.acceptance.AcceptanceUtils.응답바디값_기대값과_같은지_검증하기;
import static kuit.subway.study.acceptance.FixtureData.*;
import static kuit.subway.study.acceptance.line.LineStep.지하철_노선_조회하기;
import static kuit.subway.study.acceptance.section.SectionStep.*;
import static kuit.subway.study.acceptance.station.StationStep.지하철_역_생성하기;

public class SectionAcceptanceTest extends AcceptanceTest {
    private final int INVALID_INPUT_STATUS_CODE = 400;
    private CreateSectionRequest 구간_데이터;
    private final Long 강남역_아이디 = 1L;
    private final Long 서초역_아이디 = 2L;
    private final Long 이호선_아이디 = 1L;

    @DisplayName("하행 종점 구간을 추가할 수 있다.")
    @Test
    public void 하행종점_구간_생성_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역)
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);
        // when  { 강남역 -> 건대입구역 } 구간을 추가 요청하면
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(건대입구역_아이디, 강남역_아이디, 10L);

        ExtractableResponse<Response> extract = 지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // then 구간이 생성되고 노선 Id 를 응답으로 받는다. {서초역 -> 강남역 -> 건대입구역}

        지하철_노선_조회하기(이호선_아이디);
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }

    @DisplayName("상행 종점 구간을 추가할 수 있다.")
    @Test
    public void 상행종점_구간_생성_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역)
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);
        // when { 건대입구역 -> 서초역 } 구간을 추가 요청하면
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(서초역_아이디, 건대입구역_아이디, 1L);
        ExtractableResponse<Response> extract = 지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // then 구간이 생성되고 노선 Id 를 응답으로 받는다. {건대입구역 -> 서초역 -> 강남역 }
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }

    @DisplayName("사이 구간을 추가할 수 있다.")
    @Test
    public void 사이_구간_생성_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역 -> 건대입구역)
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(건대입구역_아이디, 강남역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // when
        CreateStationRequest 성수역_데이터 = 지하철_역_생성_데이터_만들기("성수역");
        지하철_역_생성하기(성수역_데이터);
        Long 성수역_아이디 = 4L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(성수역_아이디, 강남역_아이디, 1L);
        ExtractableResponse<Response> extract = 지하철_구간_생성하기(구간_데이터, 이호선_아이디);

        // then 구간이 생성되고 노선 Id 를 응답으로 받는다. {건대입구역 -> 서초역 -> 강남역 }
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }

    @DisplayName("새로운 구간의 하행역이 기존 구간에 존재하는 역이면, 구간 생성 요청이 거절되어야 합니다.")
    @Test
    public void 지하철_구간_중복역_생성_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역)
        이호선_강남역하행_서초역상행_생성하기();
        // when (서초역 -> 강남역) 을 추가하려고 하면
        구간_데이터 = 지하철_구간_생성_데이터_만들기(서초역_아이디, 강남역_아이디, 10L);
        ExtractableResponse<Response> extract = 지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // then 추가할 구간의 하행역은 기존 구간에 포함되지 않아야 하므로, 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("새로운 구간의 상행역이 기존 구간의 하행 종점이 아니라면, 구간 생성 요청이 거절되어야 합니다.")
    @Test
    public void 지하철_구간_생성_예외_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역)
        이호선_강남역하행_서초역상행_생성하기();
        // when {건대입구역->성수역} 구간을 추가하려고 하면
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;

        CreateStationRequest 성수역_데이터 = 지하철_역_생성_데이터_만들기("성수역");
        지하철_역_생성하기(성수역_데이터);
        Long 성수역_아이디 = 4L;

        구간_데이터 = 지하철_구간_생성_데이터_만들기(성수역_아이디, 건대입구역_아이디, 10L);
        ExtractableResponse<Response> extract = 지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // then 새로 추가할 구간의 상행역(성수역)이 기존 구간의 하행 종점(강남역)이 아니므로 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("마지막 구간에 대한 삭제 요청 시, 구간이 제거되어야 한다.")
    @Test
    public void 지하철_하행종점_구간_삭제_테스트() {
        /* given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역)
         *        (강남역 -> 건대입구역) 을 추가한다.
         *        그러면 (서초역 -> 강남역 -> 건대입구역) 이고 (강남역 -> 건대입구역) 구간의 ID 는 2L 이다. */
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);

        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        구간_데이터 = 지하철_구간_생성_데이터_만들기(3L, 강남역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디);

        지하철_노선_조회하기(이호선_아이디);
        // when (강남역 -> 건대입구역) 구간 삭제 요청을 보내면
        DeleteSectionRequest 구간_삭제_요청_데이터 = 지하철_구간_삭제_데이터_만들기(2L);
        ExtractableResponse<Response> extract = 지하철_구간_삭제하기(구간_삭제_요청_데이터, 이호선_아이디);

        // then 마지막 구간이 맞으므로 삭제되어야 한다.
        응답결과_검증하기(extract, HttpStatus.NO_CONTENT);
        지하철_노선_조회하기(이호선_아이디);
    }

    @DisplayName("마지막 구간이 아닌 구간에 대한 삭제 요청은, 거절되어야 한다.")
    @Test
    public void 지하철_하행종점이_아닌_구간_삭제_테스트() {
        /* given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역)
         *        (강남역 -> 건대입구역) 을 추가한다.
         *        그러면 (서초역 -> 강남역 -> 건대입구역) 이고 (강남역 -> 건대입구역) 구간의 ID 는 2L 이다. */
        이호선_강남역하행_서초역상행_생성하기();
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        구간_데이터 = 지하철_구간_생성_데이터_만들기(3L, 강남역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디);

        // when (서초역 -> 강남역) 구간에 대한 삭제 요청을 보내면
        DeleteSectionRequest 구간_삭제_요청_데이터 = 지하철_구간_삭제_데이터_만들기(1L);
        ExtractableResponse<Response> extract = 지하철_구간_삭제하기(구간_삭제_요청_데이터, 이호선_아이디);

        // then 해당 구간은 마지막 구간이 아니므로 삭제 요청이 거절된다.
        응답결과_검증하기(extract, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("(구간삭제 API V2) 상행 종점 구간에 대한 삭제가 가능하다.")
    @Test
    public void 지하철_상행종점구간_삭제_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역 -> 건대입구역)
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(건대입구역_아이디, 강남역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // when
        DeleteSectionRequestV2 지하철_구간_삭제_데이터 = 지하철_구간_삭제_데이터_만들기V2(강남역_아이디, 서초역_아이디);
        ExtractableResponse<Response> extract = 지하철_구간_삭제하기V2(지하철_구간_삭제_데이터, 이호선_아이디);

        // then 
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }

    @DisplayName("(구간삭제 API V2) 하행 종점 구간에 대한 삭제가 가능하다.")
    @Test
    public void 지하철_하행종점구간_삭제_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역 -> 건대입구역)
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(건대입구역_아이디, 강남역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디);
        // when
        DeleteSectionRequestV2 지하철_구간_삭제_데이터 = 지하철_구간_삭제_데이터_만들기V2(건대입구역_아이디, 강남역_아이디);
        ExtractableResponse<Response> extract = 지하철_구간_삭제하기V2(지하철_구간_삭제_데이터, 이호선_아이디);

        // then
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }

    @DisplayName("(구간삭제 API V2) 사이 구간에 대한 삭제가 가능하다.")
    @Test
    public void 지하철_사이종점구간_삭제_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고 (서초역 -> 강남역 -> 건대입구역)
        이호선_강남역하행_서초역상행_생성하기();
        지하철_노선_조회하기(이호선_아이디);
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 3L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(건대입구역_아이디, 강남역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디);

        CreateStationRequest 성수역_데이터 = 지하철_역_생성_데이터_만들기("성수역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 성수역_아이디 = 4L;
        구간_데이터 = 지하철_구간_생성_데이터_만들기(성수역_아이디, 건대입구역_아이디, 10L);
        지하철_구간_생성하기(구간_데이터, 이호선_아이디); // 서초 -> 강남 -> 건입 -> 서초
        // when
        DeleteSectionRequestV2 지하철_구간_삭제_데이터 = 지하철_구간_삭제_데이터_만들기V2(건대입구역_아이디, 강남역_아이디);
        ExtractableResponse<Response> extract = 지하철_구간_삭제하기V2(지하철_구간_삭제_데이터, 이호선_아이디);

        // then
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }
}
