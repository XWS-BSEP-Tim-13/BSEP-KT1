package com.example.backend.service;

import com.example.backend.dto.ChangePasswordDto;
import com.example.backend.dto.PasswordlessCodeRequestDto;
import com.example.backend.dto.PasswordlessLoginRequestDto;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.email_feedback.Mail;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.ForgotPasswordToken;
import com.example.backend.model.PasswordlessCredentials;
import com.example.backend.model.VerificationData;
import com.example.backend.repository.*;
import com.example.backend.service.interfaces.AuthService;
import com.example.backend.service.interfaces.MailService;
import com.example.backend.service.interfaces.EmailService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
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
    private final PasswordlessCredentialsRepository passwordlessCredentialsRepository;
    private final MailService mailService;
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

        CertificationEntity certificationEntity = certificationEntityRepository.findByEmailIncludingNotActive(verificationData.getEmail());
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

    public void generatePasswordlessCode(PasswordlessCodeRequestDto codeRequestDto) throws MessagingException, UnsupportedEncodingException {
        CertificationEntity entity = certificationEntityRepository.findByEmail(codeRequestDto.getEmail());
        if(entity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find account with this email.");
        }

        String code = getRandomNumberString();
        Date expiringDate = getTime15MinutesAfter(Calendar.getInstance());

        PasswordlessCredentials credentialsDb = passwordlessCredentialsRepository.findByEmail(codeRequestDto.getEmail());

        if(credentialsDb == null){
            PasswordlessCredentials credentials = PasswordlessCredentials.builder()
                    .code(code)
                    .email(codeRequestDto.getEmail())
                    .expiringDate(expiringDate)
                    .build();
            passwordlessCredentialsRepository.save(credentials);

            mailService.sendPasswordlessCode(credentials);
        }
        else {
            credentialsDb.setCode(code);
            credentialsDb.setExpiringDate(expiringDate);
            passwordlessCredentialsRepository.save(credentialsDb);

            mailService.sendPasswordlessCode(credentialsDb);
        }


    }

    @Override
    public boolean canUserLogInPasswordlessly(PasswordlessLoginRequestDto loginRequestDto) {
        PasswordlessCredentials credentials = passwordlessCredentialsRepository.findByEmail(loginRequestDto.getEmail());
        if(!credentials.getCode().equals(loginRequestDto.getCode())) return false;

        return true;
    }

    @Override
    public CertificationEntity findByEmail(String email) {
        return certificationEntityRepository.findByEmail(email);
    }

    private String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private Date getTime15MinutesAfter(Calendar currentTime) {
        long timeInMillis = currentTime.getTimeInMillis();
        return new Date(timeInMillis + (15 * 60 * 1000));
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
