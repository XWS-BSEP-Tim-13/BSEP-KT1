package com.example.backend.repository;

import com.example.backend.model.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationEntityRepository extends JpaRepository<CertificationEntity, Long> {
}
