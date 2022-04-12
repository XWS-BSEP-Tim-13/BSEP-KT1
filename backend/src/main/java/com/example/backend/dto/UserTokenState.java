package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenState {
    private String accessToken;
    private Integer expiresIn;
    private String email;
    private String commonName;
    private String role;
    private String organization;
}
