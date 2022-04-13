package com.example.backend.dto;

import com.example.backend.enums.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateBasicDto {

    private String issuer;
    private String subject;
    private String subjectEmail;
    private Integer id;
    private CertificateType type;
}
