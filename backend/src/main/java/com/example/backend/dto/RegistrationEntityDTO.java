package com.example.backend.dto;

import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegistrationEntityDTO {
    private String commonName;
    private String email;
    private String organization;
    private String organizationUnit;
    private String countryCode;
    private String password;
    private EntityRole entityRole;
    private String role;
}
