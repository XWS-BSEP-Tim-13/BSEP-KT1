package com.example.backend.service.interfaces;

import com.example.backend.dto.CreationCertificateDto;
import org.springframework.stereotype.Service;

import java.security.cert.Certificate;

@Service
public interface CertificateService {
    void test();
    public boolean saveCertificate(CreationCertificateDto dto);

    Certificate findCertificate(Integer certificateId);
}
