package com.example.backend.dto;

import com.example.backend.enums.CertificateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateDto {

    private Integer id;
    private Integer serialNumber;
    private List<String> purposes;
    private String issuer;
    private String subject;
    private Date validFrom;
    private Date valitdTo;
    private String signatureAlgorithm;
    private String signatureHashAlgorithm;
    private String publicKey;
    private CertificateStatus certificateStatus;
}
