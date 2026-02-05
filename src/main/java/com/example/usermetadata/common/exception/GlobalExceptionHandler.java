package com.example.usermetadata.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessException ex) {

        String requestId = MDC.get("requestId");
        log.error("RequestId={} | Business error: {}", requestId, ex.getMessage());
        ApiError error = new ApiError(
                requestId,
                ex.getErrorCode(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleSystem(Exception ex) {

        String requestId = MDC.get("requestId");
        log.error("RequestId={} | System error", requestId, ex);
        ApiError error = new ApiError(
                requestId,
                "INTERNAL_ERROR",
                "Unexpected error occurred",
                500,
                LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(error);
    }
}
