package com.example.backend.service;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateType;
import com.example.backend.keystores.KeystoreReader;
import com.example.backend.keystores.KeystoreWriter;
import com.example.backend.model.CertificationEntity;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.util.CertificateGenerator;
import com.example.backend.util.KeyPairGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

@AllArgsConstructor
@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateGenerator certificateGenerator;
    private final KeyPairGenerator keyPairGenerator;
    private final KeystoreWriter keystoreWriter;
    private final KeystoreReader keystoreReader;

    public void test(){
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

        keystoreWriter.loadKeyStore(null, "123".toCharArray());
        keystoreWriter.saveKeyStore("keystore/test.jks", "123".toCharArray());

        keystoreWriter.write("alias", issuerKeyPair.getPrivate(), "123".toCharArray(), generatedCertificate);
        Certificate loadedCertificate = keystoreReader.readCertificate("keystore/test.jks", "123", "alias");

    }
}
