package com.hunarhub.dto;

import com.hunarhub.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String email;
    private String role; // Changed to String to ensure proper serialization
    
    public AuthResponse(String token, String type, Long id, String name, String email, User.Role role) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role != null ? role.name() : null; // Convert enum to string
    }
}
