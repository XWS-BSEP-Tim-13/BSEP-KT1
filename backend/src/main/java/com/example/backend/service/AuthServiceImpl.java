package com.example.backend.service;

import com.example.backend.dto.ChangePasswordDto;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.ForgotPasswordToken;
import com.example.backend.model.VerificationData;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.ForgotPasswordTokenRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.VerificationDataRepository;
import com.example.backend.service.interfaces.AuthService;
import com.example.backend.service.interfaces.EmailService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CertificationEntityRepository certificationEntityRepository;
    private final RoleRepository roleRepository;
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final VerificationDataRepository verificationDataRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity) {
        CertificationEntity existedEntity = certificationEntityRepository.findByEmail(registrationEntity.getEmail());
        if (existedEntity != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists.");

        CertificationEntity certificationEntity = modelMapper.map(registrationEntity, CertificationEntity.class);
        certificationEntity.setPassword(passwordEncoder.encode(registrationEntity.getPassword()));
        certificationEntity.setRole(roleRepository.findByName(registrationEntity.getRole()));
        certificationEntityRepository.save(certificationEntity);

        VerificationData verificationData = saveVerificationData(registrationEntity.getEmail());
        sendVerificationEmail(verificationData);

        return certificationEntity;
    }

    @Override
    public void changePassword(ChangePasswordDto dto) throws Exception {
        if (!dto.getConfirmPassword().equals(dto.getPassword())) throw new Exception();
        ForgotPasswordToken token = forgotPasswordTokenRepository.findByToken(dto.getToken());
        CertificationEntity certificationEntity = certificationEntityRepository.findByEmail(token.getEmail());
        certificationEntity.setPassword(passwordEncoder.encode((dto.getPassword())));
        certificationEntityRepository.save(certificationEntity);
        forgotPasswordTokenRepository.delete(token);
        return;
    }

    @Override
    @Transactional
    public String activateAccount(String code) {
        VerificationData verificationData = verificationDataRepository.findByCode(code);

        if(verificationData == null) return  "Cannot find account with this activation code.";
        if(verificationData.isCodeUsed()) return  "This code has already been used.";
        if(verificationData.getExpiresAt().before(new Date())) return  "This code is expired.";

        CertificationEntity certificationEntity = certificationEntityRepository.findByEmail(verificationData.getEmail());
        if(certificationEntity == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user with this email.");

        certificationEntity.setActive(true);
        certificationEntityRepository.save(certificationEntity);

        verificationData.setCodeUsed(true);
        verificationDataRepository.save(verificationData);

        return "Account successfully activated!";
    }

    private VerificationData saveVerificationData(String email) {
        VerificationData verificationData = new VerificationData(
                UUID.randomUUID().toString(),
                false,
                email,
                new Date(new Date().getTime() + (1000 * 60 * 60 * 24))
        );

        verificationDataRepository.save(verificationData);
        return verificationData;
    }


    private void sendVerificationEmail(VerificationData verificationData) {
        emailService.sendSimpleMessage(
                verificationData.getEmail(),
                "Subject: PKI Account Activation\r\n",
                        "Please click on link to activate your account: " +
                        "http://localhost:8081/auth/activate/" + verificationData.getCode()
        );
    }
}
