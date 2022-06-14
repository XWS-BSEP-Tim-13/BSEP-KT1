package com.example.backend.service.interfaces;

import com.example.backend.dto.ChangePasswordDto;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.model.CertificationEntity;

public interface AuthService {
    CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity);
    void changePassword(ChangePasswordDto dto) throws Exception;
    String activateAccount(String code);
}
