package kuit.subway.exception.badrequest;

public class DuplicatedLineNameException extends BadRequestException {
    public DuplicatedLineNameException() {
        super("지하철 노선 이름이 중복된 값입니다.", 3002);
    }
}
