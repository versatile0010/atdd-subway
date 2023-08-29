package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.INVALID_ADD_SECTION_DISTANCE_ERROR;

public class InvalidAddSectionDistanceException extends BadRequestException{
    public InvalidAddSectionDistanceException(){
        super(INVALID_ADD_SECTION_DISTANCE_ERROR);
    }
}
