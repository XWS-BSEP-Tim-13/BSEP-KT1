package com.example.backend.service;

import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.Role;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.service.interfaces.AuthService;
import com.example.backend.service.interfaces.KeystorePasswordsService;
import com.example.backend.util.CertificateGenerator;
import com.example.backend.util.KeyPairGenerator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CertificationEntityRepository certificationEntityRepository;
    private final RoleRepository roleRepository;

    @Override
    public CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity) {
        CertificationEntity certificationEntity = modelMapper.map(registrationEntity, CertificationEntity.class);
        certificationEntity.setPassword(passwordEncoder.encode(registrationEntity.getPassword()));
        certificationEntity.setRole(roleRepository.findByName(registrationEntity.getRole()));
        certificationEntityRepository.save(certificationEntity);
        return certificationEntity;
    }
}
