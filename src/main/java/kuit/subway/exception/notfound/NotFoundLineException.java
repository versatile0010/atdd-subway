package kuit.subway.exception.notfound;

import static kuit.subway.exception.CustomExceptionContext.NOT_FOUND_LINE_ERROR;

public class NotFoundLineException extends NotFoundException {
    public NotFoundLineException() {
        super(NOT_FOUND_LINE_ERROR);
    }
}
