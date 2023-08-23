package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.INVALID_UP_STATION_ERROR;

public class InvalidUpStationException extends BadRequestException{
    public InvalidUpStationException(){
        super(INVALID_UP_STATION_ERROR);
    }
}
