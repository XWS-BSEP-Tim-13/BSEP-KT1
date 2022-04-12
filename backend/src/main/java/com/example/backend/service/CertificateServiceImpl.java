package com.example.backend.service;

import com.example.backend.dto.CertificateBasicDto;
import com.example.backend.dto.CertificateDto;
import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateStatus;
import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.Certificate;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.OrganizationKeystoreAccess;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.CertificationRepostory;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.service.interfaces.KeystorePasswordsService;
import com.example.backend.util.CertificateGenerator;
import com.example.backend.util.KeyPairGenerator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
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
    private final ModelMapper modelMapper;

    public boolean saveCertificate(CreationCertificateDto dto){
        CertificationEntity subject = certificationEntityRepository.findById(dto.getSubjectEntityId()).get();
        com.example.backend.model.Certificate issuer = certificationRepostory.findById(dto.getSignerCertificateId()).get();

        com.example.backend.model.Certificate dbCertificate = generateDBCertificate(subject, dto);

        Boolean organizationExists = checkIfOrganizationExists(subject);

        String keystorePassword = "";
        String issuerKeyStorePassword = "";

        if(!organizationExists){
            keystorePassword = createNewOrganizationPassword(subject);
            keystoreHandler.loadKeyStore(null, keystorePassword.toCharArray());
            keystoreHandler.saveKeyStore("keystore/" + subject.getOrganization() + ".jks", keystorePassword.toCharArray());
        }
        else{
            keystorePassword = passwordsService.findPasswordByOrganization(subject.getOrganization());
            issuerKeyStorePassword = keystorePassword;
        }

        if(issuer.getSubject().getEntityRole().equals(EntityRole.ADMIN)){
            issuerKeyStorePassword = passwordsService.findPasswordByOrganization(issuer.getSubject().getOrganization());
        }

        PrivateKey issuerPrivateKey = keystoreHandler.readPrivateKey(issuer.getCerFileName(), issuerKeyStorePassword, issuer.getAlias(), issuerKeyStorePassword);
        issuer.setPrivateKey(issuerPrivateKey);

        X509Certificate x509Cert = generateCertificate(dbCertificate, issuer);
        dbCertificate.setAlias(UUID.randomUUID().toString());

        keystoreHandler.loadKeyStore(dbCertificate.getCerFileName(), keystorePassword.toCharArray());
        keystoreHandler.write(dbCertificate.getAlias(), dbCertificate.getPrivateKey(), keystorePassword.toCharArray(), x509Cert);
        keystoreHandler.saveKeyStore(dbCertificate.getCerFileName(), keystorePassword.toCharArray());

        subject.getCertificates().add(dbCertificate);
        certificationEntityRepository.save(subject);

        //List<X509Certificate> allCertificates = keystoreHandler.readAllCertificates(dbCertificate.getCerFileName(), keystorePassword);

        return true;
    }

    @Override
    public List<CertificateBasicDto> findAllByType(CertificateType type) {
        List<CertificateBasicDto> list = new ArrayList<>();
        for(Certificate cert : certificationRepostory.findByType(type)){
            CertificateBasicDto dto = CertificateBasicDto.builder()
                    .type(cert.getType())
                    .issuer(cert.getParentCertificate().getSubject().getCommonName())
                    .subject(cert.getSubject().getCommonName())
                    .id(cert.getId())
                    .build();
            list.add(dto);
        }
        return  list;
    }

    @Override
    public CertificateDto findCertificateInfo(Integer certificateId) {
        Certificate cert = certificationRepostory.findById(certificateId).get();
        RSAPublicKey pubKey = (RSAPublicKey) cert.getPublicKey();
        CertificateDto retVal= modelMapper.map(cert,CertificateDto.class);
        retVal.setPublicKey(pubKey.getModulus().toString());
        retVal.setSignatureAlgorithm(pubKey.getAlgorithm());
        retVal.setValitdTo(cert.getExpiringDate());
        return retVal;
    }

    @Override
    public Certificate findDbCert(Integer certificateId) {
        return certificationRepostory.findById(certificateId).get();
    }

    @Override
    public X509Certificate findCertificate(Integer certificateId) {
        com.example.backend.model.Certificate dbCert=certificationRepostory.findById(certificateId).get();
        String password=passwordsService.findPasswordByOrganization(dbCert.getSubject().getOrganization());
        keystoreHandler.loadKeyStore(dbCert.getCerFileName(), password.toCharArray());
        return keystoreHandler.readCertificate(dbCert.getCerFileName(),password, dbCert.getAlias());
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
        OrganizationKeystoreAccess organizationKeystoreAccess = new OrganizationKeystoreAccess(keystorePassword, certificationEntity.getOrganization());
        passwordsService.saveOrganizationPassword(organizationKeystoreAccess);
        return keystorePassword;
    }

    private X509Certificate generateCertificate(com.example.backend.model.Certificate subject, com.example.backend.model.Certificate issuer){
        return certificateGenerator.generateCertificate(subject, issuer);
    }

    private com.example.backend.model.Certificate generateDBCertificate(CertificationEntity subject, CreationCertificateDto creationCertificateDto){

        boolean isCa = false;
        if(creationCertificateDto.getCertificateType().equals(CertificateType.SELF_SIGNED) || creationCertificateDto.getCertificateType().equals(CertificateType.INTERMEDIATE)){
            isCa = true;
        }

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        com.example.backend.model.Certificate parentCertificate = certificationRepostory.findById(creationCertificateDto.getSignerCertificateId()).get();

        com.example.backend.model.Certificate certificate = com.example.backend.model.Certificate.builder()
                .subject(subject)
                .type(creationCertificateDto.getCertificateType())
                .publicKey(keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .cerFileName("keystore/" + subject.getOrganization() + ".jks")
                .isCA(isCa)
                .parentCertificate(parentCertificate)
                .purposes(new ArrayList<>(creationCertificateDto.getPurposes()))
                .expiringDate(creationCertificateDto.getExpiringDate())
                .validFrom(creationCertificateDto.getValidFrom())
                .build();

        if(certificate.getValidFrom().after(new Date())) certificate.setCertificateStatus(CertificateStatus.INVALID);
        else certificate.setCertificateStatus(CertificateStatus.GOOD);
        
        return certificate;
    }
}
