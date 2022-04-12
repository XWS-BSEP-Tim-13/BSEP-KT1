package com.example.backend.service;

import com.example.backend.dto.FetchCertificateDTO;
import com.example.backend.keystores.KeystoreHandler;
import com.example.backend.model.Certificate;
import com.example.backend.repository.CertificationRepostory;
import com.example.backend.service.interfaces.FetchCertificateService;
import com.example.backend.service.interfaces.KeystorePasswordsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FetchCertificateServiceImpl implements FetchCertificateService {
    private final KeystoreHandler keystoreHandler;
    private final CertificationRepostory certificationRepostory;

    @Override
    public List<FetchCertificateDTO> getAllFromKeystore(String keystorePath, String keystorePass) {
        Enumeration<String> aliases = keystoreHandler.getAliases(keystorePath, keystorePass);
        List<FetchCertificateDTO> allCertificates = new ArrayList<>();
        while(aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            X509Certificate x509Certificate = keystoreHandler.readCertificate(keystorePath, keystorePass, alias);
            Certificate certificate = certificationRepostory.findByAlias(alias);
            FetchCertificateDTO certificateDTO = createFetchCertificateDTO(certificate, x509Certificate);
            allCertificates.add(certificateDTO);
        }

        return allCertificates;
    }


    @Override
    public List<FetchCertificateDTO> getAllCertificatesByOrganization(String organization) {
        ArrayList<Certificate> certificates = (ArrayList<Certificate>) certificationRepostory.findByOrganization(organization);
        ArrayList<FetchCertificateDTO> certificateDTOS = new ArrayList<>();
        for(Certificate cert : certificates){
            certificateDTOS.add(createFetchCertificateDTO(cert));
        }
        return certificateDTOS;

    }

    @Override
    public List<FetchCertificateDTO> getAllCertificates() {
        ArrayList<Certificate> certificates = (ArrayList<Certificate>) certificationRepostory.findAll();
        ArrayList<FetchCertificateDTO> certificateDTOS = new ArrayList<>();
        for(Certificate cert : certificates){
            certificateDTOS.add(createFetchCertificateDTO(cert));
        }
        return certificateDTOS;
    }

    @Override
    public List<FetchCertificateDTO> getAllCertificatesBySubject(Integer subjectId) {
        ArrayList<Certificate> subjectCertificates = (ArrayList<Certificate>) certificationRepostory.findBySubjectId(subjectId);
        ArrayList<FetchCertificateDTO> certificateDTOS = new ArrayList<>();
        for(Certificate cert : subjectCertificates){
            certificateDTOS.add(createFetchCertificateDTO(cert));
        }
        return certificateDTOS;
    }

    @Override
    public List<FetchCertificateDTO> getHierarchyAbove(Integer certificateId) {
        ArrayList<FetchCertificateDTO> certificateDTOS = new ArrayList<>();
        Certificate tempCertificate = certificationRepostory.findById(certificateId).get();
        certificateDTOS.add(createFetchCertificateDTO(tempCertificate));
        while(tempCertificate.getParentCertificate().getId() != tempCertificate.getId()){
            tempCertificate = tempCertificate.getParentCertificate();
            certificateDTOS.add(createFetchCertificateDTO(tempCertificate));
        }
        Collections.reverse(certificateDTOS);
        return certificateDTOS;
    }


    private FetchCertificateDTO createFetchCertificateDTO(Certificate certificate, X509Certificate x509Certificate){
        return FetchCertificateDTO.builder()
                .certificateId(certificate.getId())
                .issuerCommonName(certificate.getParentCertificate().getSubject().getCommonName())
                .subjectCommonName(certificate.getSubject().getCommonName())
                .purposes(new ArrayList<>(certificate.getPurposes()))
                .validFrom(certificate.getValidFrom())
                .expiringDate(certificate.getExpiringDate())
                .signatureAlgorithm(x509Certificate.getSigAlgName())
                .signatureHashAlgorithm("SHA256")
                .publicKey(x509Certificate.getPublicKey().toString())
                .build();
    }

    private FetchCertificateDTO createFetchCertificateDTO(Certificate certificate){
        return FetchCertificateDTO.builder()
                .certificateId(certificate.getId())
                .issuerCommonName(certificate.getParentCertificate().getSubject().getCommonName())
                .subjectCommonName(certificate.getSubject().getCommonName())
                .purposes(new ArrayList<>(certificate.getPurposes()))
                .validFrom(certificate.getValidFrom())
                .expiringDate(certificate.getExpiringDate())
                .signatureAlgorithm("SHA256withRSA")
                .signatureHashAlgorithm("SHA256")
                .publicKey(certificate.getPublicKey().toString())
                .build();
    }

}
