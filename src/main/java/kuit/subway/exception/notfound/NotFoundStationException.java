package kuit.subway.exception.notfound;

public class NotFoundStationException extends NotFoundException {
    public NotFoundStationException(){
        super("존재하지 않는 지하철 역입니다.", 2000);
    }
}
