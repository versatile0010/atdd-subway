package kuit.subway.exception.notfound;

public class NotFoundLineException extends NotFoundException {
    public NotFoundLineException() {
        super("존재하지 않는 지하철 노선입니다.", 2001);
    }
}
