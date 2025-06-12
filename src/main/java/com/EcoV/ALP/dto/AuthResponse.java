package com.EcoV.ALP.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private boolean success;
    private Long id_user;
    private String name;
    private String email;

    // Constructor for error responses
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
