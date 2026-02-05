package com.example.usermetadata.common.exception;

public class ServiceUnavailableException extends BusinessException {
    public ServiceUnavailableException() {
        super("Service temporarily unavailable",
                "SERVICE_UNAVAILABLE");
    }
}
