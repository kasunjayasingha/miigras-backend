package com.kasunjay.miigrasbackend.model;

import com.kasunjay.miigrasbackend.common.enums.Success;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class StandardResponse {

    private HttpStatus status;
    private Success success;
    private String error;
    private String message;

    public StandardResponse(HttpStatus status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
    public StandardResponse(HttpStatus status, Success success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }
}
