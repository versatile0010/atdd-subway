package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.ALREADY_EXIST_STATIONS_ERROR;

public class AlreadyExistStationsException extends BadRequestException {
    public AlreadyExistStationsException() {
        super(ALREADY_EXIST_STATIONS_ERROR);
    }
}
