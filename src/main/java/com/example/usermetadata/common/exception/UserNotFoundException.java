package com.example.usermetadata.common.exception;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id,
                "USER_NOT_FOUND");
    }
}
