package com.example.backend.service.interfaces;

import com.example.backend.dto.CertificateIssuerDTO;
import com.example.backend.dto.NewCertificateSubjectDTO;
import com.example.backend.model.CertificationEntity;

import java.util.List;
import java.util.Set;

public interface CertificationEntityService {

    Set<NewCertificateSubjectDTO> getPossibleSubjectsForNewCertificate();
    List<CertificationEntity> findAllIssuers();
    List<CertificateIssuerDTO> findIssuersByOrganization(String organization);
}
