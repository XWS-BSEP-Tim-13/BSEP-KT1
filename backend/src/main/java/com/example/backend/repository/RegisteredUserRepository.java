package com.example.backend.repository;

import com.example.backend.model.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<CertificationEntity, Long> {
}
