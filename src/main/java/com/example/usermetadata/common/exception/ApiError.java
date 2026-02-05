package com.example.usermetadata.common.exception;

import lombok.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ApiError {

    private String requestId;
    private String errorCode;
    private String message;
    private int status;
    private LocalDateTime timestamp;
}