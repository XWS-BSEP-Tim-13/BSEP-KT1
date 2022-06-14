package com.example.backend.service;

import com.example.backend.dto.ChangePasswordDto;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.ForgotPasswordToken;
import com.example.backend.model.Role;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.ForgotPasswordTokenRepository;
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
    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Override
    public CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity) {
        CertificationEntity certificationEntity = modelMapper.map(registrationEntity, CertificationEntity.class);
        certificationEntity.setPassword(passwordEncoder.encode(registrationEntity.getPassword()));
        certificationEntity.setRole(roleRepository.findByName(registrationEntity.getRole()));
        certificationEntityRepository.save(certificationEntity);
        return certificationEntity;
    }

    @Override
    public void changePassword(ChangePasswordDto dto) throws Exception {
        if(!dto.getConfirmPassword().equals(dto.getPassword())) throw new Exception();
        ForgotPasswordToken token = forgotPasswordTokenRepository.findByToken(dto.getToken());
        CertificationEntity certificationEntity= certificationEntityRepository.findByEmail(token.getEmail());
        certificationEntity.setPassword(passwordEncoder.encode((dto.getPassword())));
        certificationEntityRepository.save(certificationEntity);
        forgotPasswordTokenRepository.delete(token);
        return;
    }
}
