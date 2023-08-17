package kuit.subway.controller;

import jakarta.servlet.http.HttpServletRequest;
import kuit.subway.dto.response.ErrorResponse;
import kuit.subway.exception.SubwayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerAdvice {
    private final int INVALID_INPUT_VALUE = 400;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInputFieldException(MethodArgumentNotValidException e) {
        FieldError mainError = e.getFieldErrors().get(0);
        String[] errorInfo = Objects.requireNonNull(mainError.getDefaultMessage()).split(":");
        String message = errorInfo[0];
        return ResponseEntity.badRequest().body(new ErrorResponse(INVALID_INPUT_VALUE, message));
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> handleSubwayException(SubwayException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unhandledException(Exception e, HttpServletRequest request) {
        log.error("UnhandledException: {} {} error Message={} ",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(9999, "일시적으로 접속이 원활하지 않습니다."));
    }

}
