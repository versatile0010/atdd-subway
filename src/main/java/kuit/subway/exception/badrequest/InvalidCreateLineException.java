package kuit.subway.exception.badrequest;

public class InvalidCreateLineException extends BadRequestException {
    public InvalidCreateLineException(){
        super("상행 종점역과 하행 종점역은 같을 수 없습니다.", 3001);
    }
}
