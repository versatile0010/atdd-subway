package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.INVALID_ADD_FIRST_SECTION_ERROR;

public class InvalidAddFirstSectionException extends BadRequestException {
    public InvalidAddFirstSectionException(){
        super(INVALID_ADD_FIRST_SECTION_ERROR);
    }
}
