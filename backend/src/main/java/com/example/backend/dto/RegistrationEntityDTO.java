package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationEntityDTO {
    private String commonName;
    private String email;
    private String organization;
    private String organizationUnit;
    private String countryCode;
    private String password;
    private Boolean isSubsystem;
}
