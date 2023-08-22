package kuit.subway.exception.badrequest;

public class InvalidUpStationException extends BadRequestException{
    public InvalidUpStationException(){
        super("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.", 3003);
    }
}
