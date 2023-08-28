package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.INVALID_ADD_LAST_SECTION_ERROR;

public class InvalidAddLastSectionException extends BadRequestException {
    public InvalidAddLastSectionException() {
        super(INVALID_ADD_LAST_SECTION_ERROR);
    }
}
