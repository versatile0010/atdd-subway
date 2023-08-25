package kuit.subway.exception.notfound;

import static kuit.subway.exception.CustomExceptionContext.NOT_FOUND_SECTION_ERROR;

public class NotFoundSectionException extends NotFoundException{
    public NotFoundSectionException(){
        super(NOT_FOUND_SECTION_ERROR);
    }
}
