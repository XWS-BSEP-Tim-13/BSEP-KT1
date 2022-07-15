package com.example.backend.util;

import com.example.backend.enums.CertificateStatus;
import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.Certificate;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.OrganizationKeystoreAccess;
import com.example.backend.model.Role;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.CertificationRepostory;
import com.example.backend.repository.RoleRepository;
import com.example.backend.service.interfaces.KeystorePasswordsService;
import lombok.AllArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
    private final CertificationRepostory certificationRepostory;
    private final KeystorePasswordsService keystorePasswordsService;

    public void run(ApplicationArguments args) {
        Security.addProvider(new BouncyCastleProvider());

        KeyPair user1keyPair = keyPairGenerator.generateKeyPair();
        KeyPair user2keyPair = keyPairGenerator.generateKeyPair();
        KeyPair adminKeyPair = keyPairGenerator.generateKeyPair();

        Role adminRole = Role.builder().name("ROLE_ADMIN").build();
        Role subsystemRole = Role.builder().name("ROLE_SUBSYSTEM").build();
        Role userRole = Role.builder().name("ROLE_USER").build();

        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        roleRepository.save(subsystemRole);

        CertificationEntity admin = CertificationEntity.builder()
                .commonName("Admin")
                .countryCode("RS")
                .email("a@a.com")
                .organization("ADMIN")
                .entityRole(EntityRole.ROLE_ADMIN)
                .password("$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW")
                .organizationUnit("Katedra za automatiku")
                .certificates(new ArrayList<>())
                .role(adminRole)
                .isActive(true)
                .build();
        admin.setId(1);

        CertificationEntity user1 = CertificationEntity.builder()
                .commonName("User One")
                .countryCode("RS")
                .email("u1@u.com")
                .organization("FTN")
                .entityRole(EntityRole.ROLE_USER)
                .password("$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW")
                .organizationUnit("Katedra za informatiku")
                .certificates(new ArrayList<>())
                .role(userRole)
                .isActive(true)
                .build();
        user1.setId(2);

        CertificationEntity user2 = CertificationEntity.builder()
                .commonName("User Two")
                .countryCode("RS")
                .email("srdjansukovic@gmail.com")
                .organization("FTN")
                .entityRole(EntityRole.ROLE_USER)
                .password("$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW")
                .organizationUnit("Katedra za racunarstvo")
                .certificates(new ArrayList<>())
                .role(userRole)
                .isActive(true)
                .build();
        user2.setId(3);

        CertificationEntity user3 = CertificationEntity.builder()
                .commonName("PMF 1")
                .countryCode("RS")
                .email("u3@u.com")
                .organization("PMF")
                .entityRole(EntityRole.ROLE_SUBSYSTEM)
                .password("$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW")
                .organizationUnit("Katedra za matematiku")
                .certificates(new ArrayList<>())
                .role(subsystemRole)
                .isActive(true)
                .build();
        user3.setId(4);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 30);
        Date endDate = cal.getTime();

        Certificate adminCertificate = Certificate.builder()
                .expiringDate(endDate)
                .cerFileName("keystore/ADMIN.jks")
                .purposes(new ArrayList<>(Arrays.asList("Proves your identity to a remote computer")))
                .type(CertificateType.SELF_SIGNED)
                .isCA(true)
                .validFrom(new Date())
                .subject(admin)
                .alias("alias1")
                .publicKey(adminKeyPair.getPublic())
                .privateKey(adminKeyPair.getPrivate())
                .certificateStatus(CertificateStatus.GOOD)
                .build();
        adminCertificate.setId(1);
        adminCertificate.setParentCertificate(adminCertificate);

        Certificate user1Certificate = Certificate.builder()
                .expiringDate(endDate)
                .cerFileName("keystore/FTN.jks")
                .purposes(new ArrayList<>(Arrays.asList("Proves your identity to a remote computer")))
                .type(CertificateType.ROOT)
                .isCA(true)
                .validFrom(new Date())
                .subject(user1)
                .alias("alias2")
                .publicKey(user1keyPair.getPublic())
                .privateKey(user1keyPair.getPrivate())
                .certificateStatus(CertificateStatus.GOOD)
                .build();
        user1Certificate.setId(2);

        Certificate user2Certificate = Certificate.builder()
                .expiringDate(endDate)
                .cerFileName("keystore/FTN.jks")
                .purposes(new ArrayList<>(Arrays.asList("Proves your identity to a remote computer")))
                .type(CertificateType.INTERMEDIATE)
                .isCA(true)
                //.validFrom(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .validFrom(new Date())
                .subject(user2)
                .alias("alias3")
                .publicKey(user2keyPair.getPublic())
                .privateKey(user2keyPair.getPrivate())
                .certificateStatus(CertificateStatus.GOOD)
                .build();
        user2Certificate.setId(3);

        X509Certificate adminCert = certificateGenerator.generateCertificate(adminCertificate, adminCertificate);

        admin.getCertificates().add(adminCertificate);
        admin = entityRepository.save(admin);
        adminCertificate = admin.getCertificates().get(0);
        user1Certificate.setParentCertificate(adminCertificate);
        adminCertificate.setPrivateKey(adminKeyPair.getPrivate());

        keystoreHandler.loadKeyStore(null, "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());
        keystoreHandler.write("alias1", adminKeyPair.getPrivate(), "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray(), adminCert);
        keystoreHandler.saveKeyStore("keystore/ADMIN.jks", "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());

        X509Certificate user1Cert = certificateGenerator.generateCertificate(user1Certificate, adminCertificate);

        user1.getCertificates().add(user1Certificate);
        if(keystorePasswordsService.findPasswordByOrganization(user1.getOrganization()).equals("")) {
            keystorePasswordsService.saveOrganizationPassword(OrganizationKeystoreAccess.builder().organiation(user1.getOrganization()).password("$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW").build());
        }
        user1 = entityRepository.save(user1);
        user1Certificate = user1.getCertificates().get(0);
        user1Certificate.setPrivateKey(user1keyPair.getPrivate());
        user2Certificate.setParentCertificate(user1Certificate);


        keystoreHandler.loadKeyStore(null, "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());
        keystoreHandler.write("alias2", user1keyPair.getPrivate(), "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray(), user1Cert);
        keystoreHandler.saveKeyStore("keystore/FTN.jks", "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());

        X509Certificate user2Cert = certificateGenerator.generateCertificate(user2Certificate, user1Certificate);
        user2.getCertificates().add(user2Certificate);
        entityRepository.save(user2);
        keystoreHandler.loadKeyStore("keystore/FTN.jks", "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());
        keystoreHandler.write("alias3", user2keyPair.getPrivate(), "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray(), user2Cert);
        keystoreHandler.saveKeyStore("keystore/FTN.jks", "$2a$10$3kfQZW0qQFJIlfDcadR9UOmPwUDDz4wwkcxxAi1aQmfqZqRxAU/FW".toCharArray());

        entityRepository.save(user3);
    }
}
