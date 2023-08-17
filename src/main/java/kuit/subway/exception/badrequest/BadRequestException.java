package kuit.subway.exception.badrequest;

import kuit.subway.exception.SubwayException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends SubwayException {
    public BadRequestException(String message, int code) {
        super(HttpStatus.BAD_REQUEST, message, code);
    }
}
