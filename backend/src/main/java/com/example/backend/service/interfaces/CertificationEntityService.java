package com.example.backend.service.interfaces;

import com.example.backend.model.CertificationEntity;

import java.util.List;

public interface CertificationEntityService {
    List<CertificationEntity> findAllIssuers();
}
