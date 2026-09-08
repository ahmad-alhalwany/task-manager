package com.taskmanager.dto;

/** Simple error payload returned by the global exception handler */
public class ApiError {

    private final String message;
    private final int status;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
