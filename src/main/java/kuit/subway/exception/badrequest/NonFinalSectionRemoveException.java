package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.NON_FINAL_SECTION_REMOVE_NOT_ALLOWED;

public class NonFinalSectionRemoveException extends BadRequestException {
    public NonFinalSectionRemoveException(){
        super(NON_FINAL_SECTION_REMOVE_NOT_ALLOWED);
    }
}
