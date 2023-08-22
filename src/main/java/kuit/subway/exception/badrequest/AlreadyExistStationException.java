package kuit.subway.exception.badrequest;

public class AlreadyExistStationException extends BadRequestException {
    public AlreadyExistStationException() {
        super("이미 해당 노선에 존재하는 역을 새로운 구간으로 추가할 수 없습니다.", 3005);
    }
}
