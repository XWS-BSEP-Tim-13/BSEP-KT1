package com.example.backend.service;

import com.example.backend.dto.CertificateDto;
import com.example.backend.dto.CertificateIssuerDTO;
import com.example.backend.dto.NewCertificateSubjectDTO;
import com.example.backend.enums.CertificateStatus;
import com.example.backend.enums.CertificateType;
import com.example.backend.enums.EntityRole;
import com.example.backend.model.Certificate;
import com.example.backend.model.CertificationEntity;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.CertificationRepostory;
import com.example.backend.service.interfaces.CertificationEntityService;
import lombok.AllArgsConstructor;
import org.hibernate.id.ForeignGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CertificationEntityServiceImpl implements CertificationEntityService {

    private final CertificationEntityRepository certificationEntityRepository;
    private final CertificationRepostory certificationRepostory;
    private final ModelMapper modelMapper;

    @Override
    public Set<NewCertificateSubjectDTO> getPossibleSubjectsForNewCertificate() {
        List<CertificationEntity> allEntities = certificationEntityRepository.findAll();
        Set<NewCertificateSubjectDTO> possibleSubjects = new HashSet<>();
        for (CertificationEntity entity : allEntities) {
            if(!entity.getRole().getName().equals("ROLE_ADMIN")) {
                boolean rootForOrganizationExists = checkIfRootForOrganizationExists(entity.getOrganization());
                possibleSubjects.add(new NewCertificateSubjectDTO(entity.getEmail(), entity.getCommonName(), entity.getId(), entity.getOrganization(), rootForOrganizationExists));
            }
        }
        return possibleSubjects;
    }

    private boolean checkIfRootForOrganizationExists(String organization) {
        List<Certificate> certificates = certificationRepostory.findAll();
        for (Certificate certificate : certificates) {
            if (organization.equals(certificate.getSubject().getOrganization())) return true;
        }
        return false;
    }

    @Override
    public List<CertificationEntity> findAllIssuers() {
        return certificationEntityRepository.findAllIssuers(EntityRole.SUBSYSTEM,EntityRole.ADMIN);
    }

    public List<CertificationEntity> findIssuersByOrganization(String organization) {
        return certificationEntityRepository.findAllIssuersByOrganization(organization);
    }

    @Override
    public List<CertificateIssuerDTO> findSuitableIssuersForCertificateSigning(String organization) {
        List<CertificationEntity> issuersFromOrganization = findIssuersByOrganization(organization).stream().filter(i -> !i.getCertificates().isEmpty()).collect(Collectors.toList());
        List<CertificateIssuerDTO> ret = new ArrayList<>();
        for(CertificationEntity entity: issuersFromOrganization) {
            ret.add(CertificateIssuerDTO.builder().commonName(entity.getCommonName()).certificates(getCertificatesDto(entity.getCertificates())).id(entity.getId()).email(entity.getEmail()).build());
        }
        return ret;
    }

    private List<CertificateDto> getCertificatesDto(List<Certificate> certificates){
        List<CertificateDto> ret = new ArrayList<>();
        for(com.example.backend.model.Certificate cert: certificates)
            if(!cert.getType().equals(CertificateType.END_ENTITY) && cert.getCertificateStatus().equals(CertificateStatus.GOOD))
                ret.add(mapCertificate(cert.getId()));

        return ret;
    }

    private CertificateDto mapCertificate(Integer certificateId) {
        Certificate cert = certificationRepostory.findById(certificateId).get();
        CertificateDto retVal= modelMapper.map(cert,CertificateDto.class);
        retVal.setValitdTo(cert.getExpiringDate());
        return retVal;
    }
}
