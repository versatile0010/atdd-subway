package kuit.subway.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomExceptionContext implements ExceptionContext {
    // NOT FOUND ERROR
    NOT_FOUND_STATION_ERROR("존재하지 않는 지하철 역입니다.", 2000),
    NOT_FOUND_LINE_ERROR("존재하지 않는 지하철 노선입니다.", 2001),
    NOT_FOUND_SECTION_ERROR("존재하지 않는 지하철 구간입니다.", 2002),

    // BAD REQUEST ERROR
    DUPLICATED_STATION_NAME_ERROR("이미 존재하는 지하철 역 이름입니다.", 3000),
    INVALID_CREATE_LINE_ERROR("상행 종점역과 하행 종점역은 같을 수 없습니다.", 3001),
    DUPLICATED_LINE_NAME_ERROR("지하철 노선 이름이 중복된 값입니다.", 3002),
    INVALID_UP_STATION_ERROR("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.", 3003),
    ALREADY_EXIST_STATION_ERROR("이미 해당 노선에 존재하는 역을 새로운 구간으로 추가할 수 없습니다.", 3004),
    SINGLE_SECTION_REMOVE_NOT_ALLOWED("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없습니다.", 3005),
    NON_FINAL_SECTION_REMOVE_NOT_ALLOWED("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있습니다.", 3006);
    private final String message;
    private final int code;
}
