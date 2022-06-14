package com.example.backend.service.interfaces;

import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.model.CertificationEntity;

public interface AuthService {
    CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity);

    void activateAccount(String code);
}
