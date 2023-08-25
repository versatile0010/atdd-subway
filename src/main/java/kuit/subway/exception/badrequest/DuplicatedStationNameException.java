package kuit.subway.exception.badrequest;

import static kuit.subway.exception.CustomExceptionContext.DUPLICATED_STATION_NAME_ERROR;

public class DuplicatedStationNameException extends BadRequestException {
    public DuplicatedStationNameException(){
        super(DUPLICATED_STATION_NAME_ERROR);
    }
}
