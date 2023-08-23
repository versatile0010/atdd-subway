package kuit.subway.exception.notfound;

import kuit.subway.exception.ExceptionContext;
import kuit.subway.exception.SubwayException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends SubwayException {
    public NotFoundException(ExceptionContext context) {
        super(HttpStatus.NOT_FOUND, context.getMessage(), context.getCode());
    }
}
