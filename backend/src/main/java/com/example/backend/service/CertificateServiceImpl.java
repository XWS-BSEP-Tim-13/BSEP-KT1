package com.example.backend.service;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.OrganizationKeystoreAccess;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.CertificationRepostory;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.service.interfaces.KeystorePasswordsService;
import com.example.backend.util.CertificateGenerator;
import com.example.backend.util.KeyPairGenerator;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CertificateServiceImpl implements CertificateService {
    private final CertificateGenerator certificateGenerator;
    private final KeyPairGenerator keyPairGenerator;
    private final KeystoreHandler keystoreHandler;
    private final CertificationEntityRepository certificationEntityRepository;
    private final CertificationRepostory certificationRepostory;
    private final KeystorePasswordsService passwordsService;
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
        Certificate loadedCertificate = keystoreHandler.readCertificate("keystore/admin.jks", "123", "alias");
    }

    public boolean saveCertificate(CreationCertificateDto dto){
        CertificationEntity subject = certificationEntityRepository.findById(dto.getSubjectId()).get();
        CertificationEntity issuer = certificationEntityRepository.findById(dto.getIssuerId()).get();
//        Hibernate.initialize(issuer.getCertificates());
        if(subject.getPublicKey() == null){
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            subject.setPrivateKey(keyPair.getPrivate());
            subject.setPublicKey(keyPair.getPublic());
        }
        issuer.setPrivateKey(findPrivateKey(issuer));
        String keystorePassword = checkIfOrganizationexists(subject);

        if(keystorePassword != ""){
            keystoreHandler.loadKeyStore(null, keystorePassword.toCharArray());
            keystoreHandler.saveKeyStore("keystore/" + subject.getOrganization()+ ".jks", keystorePassword.toCharArray());
        }
        else{
            keystorePassword = passwordsService.findPasswordByOrganization(subject.getOrganization());
        }

        X509Certificate x500Cert = generateCertificate(subject,issuer,dto);
        com.example.backend.model.Certificate dbCert = generateDBCertificate(subject,issuer,dto,x500Cert);
        dbCert.setAlias(UUID.randomUUID().toString());
        keystoreHandler.loadKeyStore(dbCert.getCerFileName(), keystorePassword.toCharArray());
        keystoreHandler.write(dbCert.getAlias(), subject.getPrivateKey(), keystorePassword.toCharArray(),x500Cert);
        keystoreHandler.saveKeyStore(dbCert.getCerFileName(), keystorePassword.toCharArray());

        subject.getCertificates().add(dbCert);
        certificationEntityRepository.save(subject);

        return true;
    }
    private PrivateKey findPrivateKey(CertificationEntity issuer){
        String password=passwordsService.findPasswordByOrganization(issuer.getOrganization());
        keystoreHandler.loadKeyStore(issuer.getCertificates().get(0).getCerFileName(),password.toCharArray());
        PrivateKey pk=keystoreHandler.readPrivateKey(issuer.getCertificates().get(0).getCerFileName(),password,issuer.getCertificates().get(0).getAlias(),password);
        return pk;
    }

    private String checkIfOrganizationexists(CertificationEntity certificationEntity) {
        File dir = new File("keystore");
        for(File file : dir.listFiles()){
            if(file.getName().contains(certificationEntity.getOrganization()))
                return "";
        }
        String keystorePassword = UUID.randomUUID().toString();
        OrganizationKeystoreAccess organizationKeystoreAccess = new OrganizationKeystoreAccess(keystorePassword,certificationEntity.getOrganization());
        passwordsService.save(organizationKeystoreAccess);
        return keystorePassword;
    }

    private X509Certificate generateCertificate(CertificationEntity subject,CertificationEntity issuer, CreationCertificateDto creationCertificateDto){
        return certificateGenerator.generateCertificate(subject,issuer,creationCertificateDto);
    }

    private com.example.backend.model.Certificate generateDBCertificate(CertificationEntity subject,CertificationEntity issuer, CreationCertificateDto creationCertificateDto,X509Certificate x500Cert){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        Date endDate = cal.getTime();
        boolean isCa = false;
        if(creationCertificateDto.getCertificateType().equals(CertificateType.SELF_SIGNED) || creationCertificateDto.getCertificateType().equals(CertificateType.INTERMEDIATE)) isCa = true;
        com.example.backend.model.Certificate certificate = com.example.backend.model.Certificate.builder()
                .subject(subject)
                .type(creationCertificateDto.getCertificateType())
                .publicKey(subject.getPublicKey())
                .cerFileName("keystore/"+ subject.getOrganization()+".jks")
                .serialNumber(x500Cert.getSerialNumber().intValue())
                .isCA(isCa)
                .parentCertificate(issuer.getCertificates().get(0))
                .purpose(creationCertificateDto.getPurpose())
                .expiringDate(endDate)
                .build();
        return certificate;
    }
}
