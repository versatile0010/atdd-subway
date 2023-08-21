package kuit.subway.exception.badrequest;

public class DuplicatedStationNameException extends BadRequestException {
    public DuplicatedStationNameException(){
        super("이미 존재하는 지하철 역 이름입니다.", 3000);
    }
}
