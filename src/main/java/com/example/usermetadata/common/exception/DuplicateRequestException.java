package com.example.usermetadata.common.exception;

public class DuplicateRequestException extends BusinessException {
    public DuplicateRequestException(String key) {
        super("Duplicate request: " + key,
                "DUPLICATE_REQUEST");
    }
}
