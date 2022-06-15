package com.example.backend.repository;

import com.example.backend.model.CertificationEntity;
import com.example.backend.model.PasswordlessCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordlessCredentialsRepository extends JpaRepository<PasswordlessCredentials, Integer> {
    PasswordlessCredentials findByEmail(String email);
}
