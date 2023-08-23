package kuit.subway.exception.notfound;

import static kuit.subway.exception.CustomExceptionContext.NOT_FOUND_STATION_ERROR;

public class NotFoundStationException extends NotFoundException {
    public NotFoundStationException(){
        super(NOT_FOUND_STATION_ERROR);
    }
}
