package com.example.backend.service.interfaces;

import com.example.backend.dto.FetchCertificateDTO;

import java.util.List;

public interface FetchCertificateService {
    List<FetchCertificateDTO> getAllFromKeystore(String keystorePath, String keystorePass);
    List<FetchCertificateDTO> getAllCertificatesByOrganization(String organization);
    List<FetchCertificateDTO> getAllCertificates();
    List<FetchCertificateDTO> getAllCertificatesBySubject(Integer subjectId);
    List<FetchCertificateDTO> getHierarchyAbove(Integer certificateId);
}
