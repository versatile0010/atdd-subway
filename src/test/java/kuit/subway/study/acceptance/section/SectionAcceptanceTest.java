package kuit.subway.study.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import jdk.jfr.Description;
import kuit.subway.AcceptanceTest;
import kuit.subway.dto.request.CreateSectionRequest;
import kuit.subway.dto.request.CreateStationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kuit.subway.study.acceptance.AcceptanceUtils.응답결과_검증하기;
import static kuit.subway.study.acceptance.AcceptanceUtils.응답바디값_기대값과_같은지_검증하기;
import static kuit.subway.study.acceptance.FixtureData.이호선_강남역하행_서초역상행_생성하기;
import static kuit.subway.study.acceptance.FixtureData.지하철_역_생성_데이터_만들기;
import static kuit.subway.study.acceptance.section.SectionStep.지하철_구간_생성하기;
import static kuit.subway.study.acceptance.station.StationStep.지하철_역_생성하기;

public class SectionAcceptanceTest extends AcceptanceTest {
    private final int INVALID_INPUT_STATUS_CODE = 400;
    private CreateSectionRequest 구간_데이터;

    @Description("지하철 구간 생성 요청이 올바르면, 구간이 생성되어야 한다.")
    @Test
    public void 지하철_구간_생성_테스트() {
        // given 강남역하행과 서초역상행인 2호선을 생성하고
        이호선_강남역하행_서초역상행_생성하기();
        // when
        CreateStationRequest 건대입구역_데이터 = 지하철_역_생성_데이터_만들기("건대입구역");
        지하철_역_생성하기(건대입구역_데이터);
        구간_데이터 = new CreateSectionRequest(3L, 1L);

        ExtractableResponse<Response> extract = 지하철_구간_생성하기(구간_데이터, 1L);
        // then 구간 Id 를 응답으로 받는다.
        응답결과_검증하기(extract, HttpStatus.CREATED);
        응답바디값_기대값과_같은지_검증하기(extract, "id", 1);
    }
}
