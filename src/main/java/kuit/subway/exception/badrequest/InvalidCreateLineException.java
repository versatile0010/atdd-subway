package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.INVALID_CREATE_LINE_ERROR;

public class InvalidCreateLineException extends BadRequestException {
    public InvalidCreateLineException(){
        super(INVALID_CREATE_LINE_ERROR);
    }
}
