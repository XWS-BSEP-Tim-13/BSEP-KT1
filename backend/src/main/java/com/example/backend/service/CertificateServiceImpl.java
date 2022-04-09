package com.example.backend.service;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import com.example.backend.keystores.KeystoreReader;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.CertificationEntity;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.util.CertificateGenerator;
import com.example.backend.util.KeyPairGenerator;
import com.example.backend.util.ParseCertificate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateGenerator certificateGenerator;
    private final KeyPairGenerator keyPairGenerator;
    private final KeystoreHandler keystoreHandler;
    private final ParseCertificate parseCertificate;

    public void test(){
        KeyPair issuerKeyPair = keyPairGenerator.generateKeyPair();
        KeyPair subjectKeyPair = keyPairGenerator.generateKeyPair();
        CertificationEntity subjectData = CertificationEntity.builder()
                .commonName("Admin")
                .countryCode("RS")
                .email("a@a.com")
                .organization("FTN")
                .entityRole(EntityRole.ADMIN)
                .password("123")
                .organizationUnit("Katedra za automatiku")
                .publicKey(issuerKeyPair.getPublic())
                .build();
        subjectData.setId(1L);

        CertificationEntity issuerData = CertificationEntity.builder()
                .commonName("Admin")
                .countryCode("RS")
                .email("a@a.com")
                .organization("FTN")
                .entityRole(EntityRole.ADMIN)
                .password("123")
                .organizationUnit("Katedra za automatiku")
                .privateKey(issuerKeyPair.getPrivate())
                .build();
        issuerData.setId(1L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 30);
        Date endDate = cal.getTime();

        CreationCertificateDto creationCertificateDto = CreationCertificateDto.builder()
                .certificateType(CertificateType.SELF_SIGNED)
                .expiringDate(endDate)
                .purpose("Proves your identity to a remote computer")
                .build();

        X509Certificate generatedCertificate = certificateGenerator.generateCertificate(subjectData, issuerData, creationCertificateDto);

        keystoreHandler.loadKeyStore(null, "123".toCharArray());
        //keystoreHandler.saveKeyStore("keystore/test.jks", "123".toCharArray());

        keystoreHandler.write("alias", issuerKeyPair.getPrivate(), "123".toCharArray(), generatedCertificate);
        keystoreHandler.saveKeyStore("keystore/admin.jks", "123".toCharArray());
        X509Certificate loadedCertificate = keystoreHandler.readCertificate("keystore/admin.jks", "123", "alias");
        X500Principal pr = loadedCertificate.getIssuerX500Principal();
        CertificationEntity en = parseCertificate.parseX500Principal(pr);
    }
}
