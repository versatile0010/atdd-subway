package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.DUPLICATED_STATION_NAME_ERROR;

public class DuplicatedLineNameException extends BadRequestException {
    public DuplicatedLineNameException() {
        super(DUPLICATED_STATION_NAME_ERROR);
    }
}
