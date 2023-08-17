package kuit.subway.exception.notfound;

import kuit.subway.exception.SubwayException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends SubwayException {
    public NotFoundException(String message, int code) {
        super(HttpStatus.NOT_FOUND, message, code);
    }
}
