package com.example.backend.dto;

import com.example.backend.enums.CertificateType;
import com.example.backend.model.Certificate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreationCertificateDto {

    private Date validFrom;
    private Date expiringDate;
    private CertificateType certificateType;
    private List<String> purposes;
    private Integer subjectEntityId;
    private Integer signerCertificateId;
}
