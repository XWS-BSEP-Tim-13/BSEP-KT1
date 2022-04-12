package com.example.backend.repository;

import com.example.backend.enums.CertificateType;
import com.example.backend.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepostory extends JpaRepository<Certificate, Integer> {

    List<Certificate> findByType(CertificateType type);
}
