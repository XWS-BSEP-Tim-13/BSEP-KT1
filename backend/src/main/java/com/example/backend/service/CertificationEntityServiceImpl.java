package com.example.backend.service;

import com.example.backend.dto.CertificateDto;
import com.example.backend.dto.CertificateIssuerDTO;
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
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CertificationEntityServiceImpl implements CertificationEntityService {

    private final CertificationEntityRepository certificationEntityRepository;
    private final CertificationRepostory certificationRepostory;
    private final ModelMapper modelMapper;

    @Override
    public List<CertificationEntity> findAllIssuers() {
        return certificationEntityRepository.findAllIssuers(EntityRole.SUBSYSTEM,EntityRole.ADMIN);
    }

    @Override
    public List<CertificateIssuerDTO> findIssuersByOrganization(String organization) {
        List<CertificationEntity> issuersFromOrganization = findAllIssuers().stream().filter(i -> i.getOrganization().equals(organization) && !i.getCertificates().isEmpty()).collect(Collectors.toList());
        List<CertificateIssuerDTO> ret = new ArrayList<>();
        for(CertificationEntity entity: issuersFromOrganization) {
            ret.add(CertificateIssuerDTO.builder().commonName(entity.getCommonName()).certificates(getCertificatesDto(entity.getCertificates())).email(entity.getEmail()).build());
        }
        return ret;
    }

    private List<CertificateDto> getCertificatesDto(List<Certificate> certificates){
        List<CertificateDto> ret = new ArrayList<>();
        for(com.example.backend.model.Certificate cert: certificates)
            if(!cert.getType().equals(CertificateType.END_ENTITY))
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
