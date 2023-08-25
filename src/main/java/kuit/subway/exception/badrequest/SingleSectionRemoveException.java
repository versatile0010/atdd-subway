package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.SINGLE_SECTION_REMOVE_NOT_ALLOWED;

public class SingleSectionRemoveException extends BadRequestException {
    public SingleSectionRemoveException() {
        super(SINGLE_SECTION_REMOVE_NOT_ALLOWED);
    }
}
