package com.example.backend.service.interfaces;

import com.example.backend.dto.ChangePasswordDto;
import com.example.backend.dto.PasswordlessCodeRequestDto;
import com.example.backend.dto.PasswordlessLoginRequestDto;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.model.CertificationEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface AuthService {
    CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity);
    void changePassword(ChangePasswordDto dto) throws Exception;
    void generatePasswordlessCode(PasswordlessCodeRequestDto codeRequestDto) throws MessagingException, UnsupportedEncodingException, NoSuchAlgorithmException;
    boolean canUserLogInPasswordlessly(PasswordlessLoginRequestDto loginRequestDto);
    CertificationEntity findByEmail(String email);
    String activateAccount(String code);
}
