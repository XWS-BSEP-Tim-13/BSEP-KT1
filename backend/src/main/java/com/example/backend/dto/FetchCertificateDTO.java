package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchCertificateDTO {

    private Integer certificateId;
    private String subjectCommonName;
    private String issuerCommonName;
    private ArrayList<String> purposes;
    private Date validFrom;
    private Date expiringDate;
    private String signatureAlgorithm;
    private String signatureHashAlgorithm;
    private String publicKey;
}
