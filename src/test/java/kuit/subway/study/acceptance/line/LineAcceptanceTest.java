package kuit.subway.study.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jdk.jfr.Description;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateLineRequest;
import kuit.subway.dto.request.CreateStationRequest;
import kuit.subway.dto.request.ModifyLineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kuit.subway.study.acceptance.line.LineStep.*;
import static kuit.subway.study.acceptance.station.StationStep.*;
import static kuit.subway.study.acceptance.AcceptanceUtils.응답결과_검증하기;
import static kuit.subway.study.acceptance.AcceptanceUtils.응답바디값_기대값과_같은지_검증하기;
import static kuit.subway.study.acceptance.FixtureData.*;

public class LineAcceptanceTest extends AcceptanceTest {
    private final int INVALID_INPUT_STATUS_CODE = 400;

    private CreateLineRequest 이호선_데이터;
    private final Long 강남역_아이디 = 1L;
    private final Long 서초역_아이디 = 2L;
    private final Long 이호선_아이디 = 1L;

    @BeforeEach
    public void 테스트를_위한_이호선_요청데이터_생성() {
        이호선_데이터 = 지하철_노선_생성_데이터_만들기("2호선", "green", 10, 1L, 2L);
    }

    @Description("지하철 노선 요청이 올바르면, 노선이 생성되어야 한다.")
    @Test
    public void 지하철_노선_생성_테스트() {
        // given 강남역과 서초역을 생성하고
        강남역_서초역_생성하기();
        // when 이호선 노선에 상행 종점역과 하행 종점역으로 생성 요청을 보내면
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(이호선_데이터);
        // then 이호선의 Id 를 응답으로 받는다.
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }

    @Description("동일한 지하철 역으로 노선 생성 시도 시, 거절되어야 한다.")
    @Test
    public void 동일_역에_대한_지하철_노선_생성_테스트() {
        // given 강남역과 서초역을 생성하고
        강남역_서초역_생성하기();
        // when 상행 종점역과 하행 종점역을 중복하여 노선 생성 요청을 보내면
        CreateLineRequest 잘못된_이호선_데이터 = 지하철_노선_생성_데이터_만들기("2호선", "green", 10, 강남역_아이디, 강남역_아이디);
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(잘못된_이호선_데이터);
        // then 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.BAD_REQUEST);
    }

    @Description("중복된 지하철 노선 이름으로 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 중복된_지하철_노선_생성_테스트() {
        // given 강남역과 서초역을 생성하고, 두 역으로 이루어진 이호선을 생성한다.
        강남역_서초역_생성하기();
        지하철_노선_생성하기(이호선_데이터);
        // when 이미 존재하는 동일한 이름의 노선(이호선)으로 생성 요청을 보내면
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(이호선_데이터);
        // then 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.BAD_REQUEST);
    }

    @Description("올바르지 않은 이름으로 노선 생성 요청 시, 거절되어야 한다.")
    @Test
    public void 올바르지않은_이름으로_지하철_노선_생성_테스트() {
        // given 강남역과 서초역을 생성하고
        강남역_서초역_생성하기();
        // when 최소 길이보다 짧은 이름의 노선으로 생성 요청을 보내면
        CreateLineRequest 짧은이름의_노선_데이터 = 지하철_노선_생성_데이터_만들기("짧음", "green", 10, 강남역_아이디, 서초역_아이디);
        ExtractableResponse<Response> extract = 지하철_노선_생성하기(짧은이름의_노선_데이터);
        // then 거절되어야 한다.
        응답결과_검증하기(extract, INVALID_INPUT_STATUS_CODE);
    }

    @Description("올바른 요청으로 노선을 조회한 경우, 노선 정보가 올바르게 조회되어야 한다.")
    @Test
    public void 지하철_노선_조회_테스트() {
        // given 강남역과 서초역을 생성하고, 두 역으로 이루어진 이호선을 생성한다.
        강남역_서초역_생성하기();
        지하철_노선_생성하기(이호선_데이터);
        // when 해당 지하철 노선을 조회하면
        ExtractableResponse<Response> extract = 지하철_노선_조회하기(이호선_아이디);
        // then 잘 조회되어야 한다.
        응답결과_검증하기(extract, HttpStatus.OK);
    }

    @Description("존재하지 않는 노선을 조회하려고 하는 경우, 거절되어야 한다.")
    @Test
    public void 존재하지_않는_지하철_노선_조회_테스트() {
        // given & when 존재하지 않는 노선을 조회하려 하면
        ExtractableResponse<Response> extract = 지하철_노선_조회하기(1L);
        // then 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.NOT_FOUND);
    }

    @Description("올바른 요청으로 노선을 수정 요청한 경우, 노선 정보가 올바르게 수정되어야 한다.")
    @Test
    public void 지하철_노선_수정_테스트() {
        // given 강남역과 서초역을 생성하고, 두 역으로 이루어진 이호선을 생성한다.
        강남역_서초역_생성하기();
        지하철_노선_생성하기(이호선_데이터);
        // when 이호선을 수정하려고 요청하면
        CreateStationRequest 어린이대공원역_데이터 = 지하철_역_생성_데이터_만들기("어린이대공원역");
        지하철_역_생성하기(어린이대공원역_데이터);
        Long 어린이대공원역_아이디 = 3L;

        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        Long 건대입구역_아이디 = 4L;

        ModifyLineRequest 노선_수정_데이터 = 지하철_노선_수정_데이터_만들기("경춘선", "red", 99, 어린이대공원역_아이디, 건대입구역_아이디);
        ExtractableResponse<Response> extract = 지하철_노선_수정하기(노선_수정_데이터, 1L);

        // 수정되어야 한다.
        응답결과_검증하기(extract, HttpStatus.CREATED);
        지하철_노선_조회하기(1L);
    }

    @Description("존재하지 않는 노선을 수정 요청한 경우, 거절되어야 한다.")
    @Test
    public void 존재하지_않는_지하철_노선_수정_테스트() {
        // when & given 존재하지 않는 다른 노선을 수정하려고 요청하면
        ModifyLineRequest 노선_수정_데이터 = 지하철_노선_수정_데이터_만들기("경춘선", "red", 99, 1L, 2L);
        ExtractableResponse<Response> extract = 지하철_노선_수정하기(노선_수정_데이터, 99L);
        // 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.NOT_FOUND);
    }

    @Description("존재하는 노선을 삭제 요청한 경우, 삭제되어야 한다.")
    @Test
    public void 지하철_노선_삭제_테스트() {
        // given 강남역과 서초역을 생성하고, 두 역으로 이루어진 이호선을 생성한다.
        강남역_서초역_생성하기();
        지하철_노선_생성하기(이호선_데이터);
        // when 이호선 삭제 요청 시
        ExtractableResponse<Response> extract1 = 지하철_노선_삭제하기(이호선_아이디);
        // then 삭제 되어야 하고, 해당 노선 정보 조회 요청은 거절되어야 한다.
        응답결과_검증하기(extract1, HttpStatus.NO_CONTENT);
        ExtractableResponse<Response> extract2 = 지하철_노선_조회하기(이호선_아이디);
        응답결과_검증하기(extract2, HttpStatus.NOT_FOUND);
    }

    @Description("존재하지 않는 노선을 삭제 요청한 경우, 거절되어야 한다.")
    @Test
    public void 존재하지_않는_지하철_노선_삭제_테스트() {
        // when & given 존재하지 않는 지하철 노선을 삭제 요청 시
        ExtractableResponse<Response> extract = 지하철_노선_삭제하기(99L);
        // then 거절되어야 한다.
        응답결과_검증하기(extract, HttpStatus.NOT_FOUND);
    }
}
