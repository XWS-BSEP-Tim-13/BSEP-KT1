package com.example.backend.service;

import com.example.backend.enums.EntityRole;
import com.example.backend.model.CertificationEntity;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.service.interfaces.CertificationEntityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CertificationEntityServiceImpl implements CertificationEntityService {

    private final CertificationEntityRepository certificationEntityRepository;

    @Override
    public List<CertificationEntity> findAllIssuers() {
        return certificationEntityRepository.findAllIssuers(EntityRole.SUBSYSTEM,EntityRole.ADMIN);
    }
}
