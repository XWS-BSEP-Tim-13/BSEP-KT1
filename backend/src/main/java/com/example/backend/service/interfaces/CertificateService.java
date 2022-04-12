package com.example.backend.service.interfaces;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.dto.NewCertificateSubjectDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public interface CertificateService {

    boolean saveCertificate(CreationCertificateDto dto);
    Set<NewCertificateSubjectDTO> getPossibleSubjectsForNewCertificate();
}
