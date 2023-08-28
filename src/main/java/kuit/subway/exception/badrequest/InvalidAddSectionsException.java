package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.INVALID_ADD_SECTION_ERROR;

public class InvalidAddSectionsException extends BadRequestException{
    public InvalidAddSectionsException(){
        super(INVALID_ADD_SECTION_ERROR);
    }
}
