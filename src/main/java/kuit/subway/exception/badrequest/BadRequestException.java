package kuit.subway.exception.badrequest;

import kuit.subway.exception.ExceptionContext;
import kuit.subway.exception.SubwayException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends SubwayException {
    public BadRequestException(ExceptionContext context) {
        super(HttpStatus.BAD_REQUEST, context.getMessage(), context.getCode());
    }
}
