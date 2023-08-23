package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.ALREADY_EXIST_STATION_ERROR;

public class AlreadyExistStationException extends BadRequestException {
    public AlreadyExistStationException() {
        super(ALREADY_EXIST_STATION_ERROR);
    }
}
