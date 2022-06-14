package com.example.backend.service;

import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.VerificationData;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.VerificationDataRepository;
import com.example.backend.service.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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
    private final VerificationDataRepository verificationDataRepository;

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
    }
}
