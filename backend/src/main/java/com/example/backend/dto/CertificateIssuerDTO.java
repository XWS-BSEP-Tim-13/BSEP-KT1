package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateIssuerDTO {
    private String commonName;
    private String email;
    private Integer id;
    private List<CertificateDto> certificates;
}
