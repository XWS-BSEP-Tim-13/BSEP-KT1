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
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

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
                .purposes(new ArrayList<String>(Arrays.asList("Proves your identity to a remote computer")))
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

        if(subject.getPublicKey() == null){
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            subject.setPrivateKey(keyPair.getPrivate());
            subject.setPublicKey(keyPair.getPublic());
        }
        issuer.setPrivateKey(findIssuerPrivateKey(issuer));

        Boolean organizationExists = checkIfOrganizationExists(subject);

        String keystorePassword = "";

        if(!organizationExists){
            keystorePassword = createNewOrganizationPassword(subject);
            keystoreHandler.loadKeyStore(null, keystorePassword.toCharArray());
            keystoreHandler.saveKeyStore("keystore/" + subject.getOrganization()+ ".jks", keystorePassword.toCharArray());
        }
        else{
            keystorePassword = passwordsService.findPasswordByOrganization(subject.getOrganization());
        }

        X509Certificate x500Cert = generateCertificate(subject, issuer, dto);
        com.example.backend.model.Certificate dbCert = generateDBCertificate(subject, issuer, dto, x500Cert);
        dbCert.setAlias(UUID.randomUUID().toString());
        keystoreHandler.loadKeyStore(dbCert.getCerFileName(), keystorePassword.toCharArray());
        keystoreHandler.write(dbCert.getAlias(), issuer.getPrivateKey(), issuer.getPassword().toCharArray(), x500Cert);
        keystoreHandler.saveKeyStore(dbCert.getCerFileName(), keystorePassword.toCharArray());

        subject.getCertificates().add(dbCert);
        certificationEntityRepository.save(subject);

        return true;
    }

    private PrivateKey findIssuerPrivateKey(CertificationEntity issuer){
        String keystorePassword = passwordsService.findPasswordByOrganization(issuer.getOrganization());

        // used only to retrieve private key, any issuer certificate will suffice, so we take first
        String keystoreFileName = issuer.getCertificates().get(0).getCerFileName();
        String certificateAlias = issuer.getCertificates().get(0).getAlias();

        keystoreHandler.loadKeyStore(keystoreFileName, keystorePassword.toCharArray());
        PrivateKey pk = keystoreHandler.readPrivateKey(keystoreFileName, keystorePassword, certificateAlias, issuer.getPassword());
        return pk;
    }

    private Boolean checkIfOrganizationExists(CertificationEntity certificationEntity) {
        File dir = new File("keystore");
        for(File file : dir.listFiles()){
            if(file.getName().contains(certificationEntity.getOrganization()))
                return true;
        }
        return false;
    }

    private String createNewOrganizationPassword(CertificationEntity certificationEntity){
        String keystorePassword = UUID.randomUUID().toString();
        OrganizationKeystoreAccess organizationKeystoreAccess = new OrganizationKeystoreAccess(keystorePassword,certificationEntity.getOrganization());
        passwordsService.saveOrganizationPassword(organizationKeystoreAccess);
        return keystorePassword;
    }

    private X509Certificate generateCertificate(CertificationEntity subject,CertificationEntity issuer, CreationCertificateDto creationCertificateDto){
        return certificateGenerator.generateCertificate(subject, issuer, creationCertificateDto);
    }

    private com.example.backend.model.Certificate generateDBCertificate(CertificationEntity subject,CertificationEntity issuer, CreationCertificateDto creationCertificateDto,X509Certificate x500Cert){

        boolean isCa = false;
        if(creationCertificateDto.getCertificateType().equals(CertificateType.SELF_SIGNED) || creationCertificateDto.getCertificateType().equals(CertificateType.INTERMEDIATE)){
            isCa = true;
        }

        com.example.backend.model.Certificate parentCertificate = certificationRepostory.getById(creationCertificateDto.getSignerCertificateId());

        com.example.backend.model.Certificate certificate = com.example.backend.model.Certificate.builder()
                .subject(subject)
                .type(creationCertificateDto.getCertificateType())
                .publicKey(subject.getPublicKey())
                .cerFileName("keystore/" + subject.getOrganization() + ".jks")
                .serialNumber(x500Cert.getSerialNumber().intValue())
                .isCA(isCa)
                .parentCertificate(parentCertificate)
                .purposes(new ArrayList<>(creationCertificateDto.getPurposes()))
                .expiringDate(creationCertificateDto.getExpiringDate())
                .build();

        return certificate;
    }
}
