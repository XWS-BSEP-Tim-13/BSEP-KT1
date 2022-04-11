package com.example.backend.repository;

import com.example.backend.enums.EntityRole;
import com.example.backend.model.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationEntityRepository extends JpaRepository<CertificationEntity, Long> {

    @Query("select a from CertificationEntity a where a.entityRole = ?1")
    CertificationEntity findAdmin(EntityRole role);
}
