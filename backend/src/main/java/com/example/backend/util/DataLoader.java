package com.example.backend.util;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.Certificate;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.Role;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.*;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private CertificationEntityRepository certificationEntityRepository;
    private final KeyPairGenerator keyPairGenerator;
    private final CertificateGenerator certificateGenerator;
    private final CertificationEntityRepository entityRepository;
    private final KeystoreHandler keystoreHandler;
    private final RoleRepository roleRepository;

    public void run(ApplicationArguments args) {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair issuerKeyPair = keyPairGenerator.generateKeyPair();
        Role adminRole = Role.builder().name("ROLE_ADMIN").build();
        Role subsystemRole = Role.builder().name("ROLE_SUBSYSTEM").build();
        Role userRole = Role.builder().name("ROLE_USER").build();
        CertificationEntity admin = CertificationEntity.builder()
                .commonName("Admin")
                .countryCode("RS")
                .email("a@a.com")
                .organization("FTN")
                .entityRole(EntityRole.ADMIN)
                .password("$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW")
                .organizationUnit("Katedra za automatiku")
                .privateKey(issuerKeyPair.getPrivate())
                .publicKey(issuerKeyPair.getPublic())
                .certificates(new ArrayList<>())
                .role(adminRole)
                .build();
        admin.setId(1L);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 30);
        Date endDate = cal.getTime();

        CreationCertificateDto creationCertificateDto = CreationCertificateDto.builder()
                .certificateType(CertificateType.SELF_SIGNED)
                .expiringDate(endDate)
                .purposes(new ArrayList<String>(Arrays.asList("Proves your identity to a remote computer")))
                .validFrom(new Date())
                .signerCertificateId(1L)
                .build();

        Certificate certificate = Certificate.builder()
                .expiringDate(endDate)
                .cerFileName("keystore/admin.jks")
                .purposes(new ArrayList<String>(Arrays.asList("Proves your identity to a remote computer")))
                .type(CertificateType.SELF_SIGNED)
                .isCA(true)
                .publicKey(issuerKeyPair.getPublic())
                .validFrom(new Date())
                .subject(admin)
                .alias("alias")
                .build();
        X509Certificate cert =certificateGenerator.generateCertificate(admin, admin, creationCertificateDto);
        certificate.setSerialNumber(cert.getSerialNumber().intValue());
        admin.getCertificates().add(certificate);
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        roleRepository.save(subsystemRole);
        entityRepository.save(admin);
        keystoreHandler.loadKeyStore(null, "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());
        //keystoreHandler.saveKeyStore("keystore/test.jks", "123".toCharArray());

        keystoreHandler.write("alias", issuerKeyPair.getPrivate(), "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray(), cert);
        keystoreHandler.saveKeyStore("keystore/admin.jks", "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());

    }
}
