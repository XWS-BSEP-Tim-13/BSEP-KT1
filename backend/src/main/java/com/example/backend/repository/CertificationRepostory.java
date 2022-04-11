package com.example.backend.repository;

import com.example.backend.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationRepostory extends JpaRepository<Certificate, Integer> {
}
