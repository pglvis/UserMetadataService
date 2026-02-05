package com.example.usermetadata.common.exception;

public class DatabaseException extends BusinessException {
    public DatabaseException(String msg, Throwable cause) {
        super(msg, "DATABASE_ERROR");
        initCause(cause);
    }
}
