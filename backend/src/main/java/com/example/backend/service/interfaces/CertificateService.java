package com.example.backend.service.interfaces;

import com.example.backend.dto.CertificateBasicDto;
import com.example.backend.dto.CertificateDto;
import com.example.backend.dto.CreationCertificateDto;

import com.example.backend.enums.CertificateType;
import com.example.backend.model.Certificate;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import com.example.backend.dto.NewCertificateSubjectDTO;
import java.util.Set;

@Service
public interface CertificateService {

    Certificate findDbCert(Integer certificateId);
    X509Certificate findCertificate(Integer certificateId);
    boolean saveCertificate(CreationCertificateDto dto);
    List<CertificateBasicDto> findAllByType(CertificateType type);
    CertificateDto findCertificateInfo(Integer certificateId);
    Boolean isCertificateRevoked(Integer id);
    Boolean revokeCertificate(Integer id);
    Set<NewCertificateSubjectDTO> getPossibleSubjectsForNewCertificate();
    boolean isCertificateValidByDate(Integer certificateId);
    boolean isCertificateValidByDate(Date certificateValidFrom, Date certificateExpiringDate);
}
