package com.example.backend.dto;

import com.example.backend.enums.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreationCertificateDto {

    private Date expiringDate;
    private CertificateType certificateType;
    private String purpose;
}
