package com.example.backend.service;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.enums.CertificateType;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.CertificationEntity;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.service.interfaces.AuthService;
import com.example.backend.util.KeyPairGenerator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final KeyPairGenerator keyPairGenerator;
    private final KeystoreHandler keystoreHandler;
    private final CertificationEntityRepository certificationEntityRepository;

    @Override
    public CertificationEntity registerCertificationEntity(RegistrationEntityDTO registrationEntity) {
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        CertificationEntity certificationEntity = modelMapper.map(registrationEntity, CertificationEntity.class);

        certificationEntity.setAlias(UUID.randomUUID().toString());
        certificationEntity.setPrivateKey(keyPair.getPrivate());
        certificationEntity.setPublicKey(keyPair.getPublic());

        keystoreHandler.loadKeyStore(null, "123".toCharArray());
        keystoreHandler.writePrivateKey(certificationEntity.getAlias(), certificationEntity.getPrivateKey(), "123".toCharArray());
        keystoreHandler.saveKeyStore("keystore/" + certificationEntity.getOrganization(), "123".toCharArray());

        certificationEntityRepository.save(certificationEntity);

        return certificationEntity;
    }

    private Certificate generateRegistrationCertificate(){
        KeyPair issuerKeyPair = keyPairGenerator.generateKeyPair();
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair();
        CertificationEntity subjectData = CertificationEntity.builder()
                .commonName("Subject Testic")
                .countryCode("RS")
                .email("a@a.com")
                .organization("FTN")
                .isSubsystem(false)
                .password("bla")
                .organizationUnit("Katedra")
                .publicKey(subjectKeyPair.getPublic())
                .build();
        subjectData.setId(1L);

        CertificationEntity issuerData = CertificationEntity.builder()
                .commonName("Issuer Issueric")
                .countryCode("RS")
                .email("b@a.com")
                .organization("FTN")
                .isSubsystem(false)
                .password("bla")
                .organizationUnit("Katedra")
                .privateKey(issuerKeyPair.getPrivate())
                .build();
        issuerData.setId(2L);

        CreationCertificateDto creationCertificateDto = CreationCertificateDto.builder()
                .certificateType(CertificateType.INTERMEDIATE)
                .expiringDate(new Date())
                .purpose("Neka namena")
                .build();

        X509Certificate generatedCertificate = certificateGenerator.generateCertificate(subjectData, issuerData, creationCertificateDto);
        return null;
    }

}
