package com.taskmanager.dto;

/** Response after successful register/login — client stores the JWT for later requests. */
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;

    public AuthResponse(String token, Long userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
