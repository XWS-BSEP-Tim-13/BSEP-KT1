package com.example.backend.service.interfaces;

import com.example.backend.dto.CreationCertificateDto;
import org.springframework.stereotype.Service;

@Service
public interface CertificateService {
    boolean saveCertificate(CreationCertificateDto dto);
    Boolean revokeCertificate(Integer id);
}
