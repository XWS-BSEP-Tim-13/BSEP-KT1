package com.example.backend.repository;

import com.example.backend.enums.EntityRole;
import com.example.backend.model.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationEntityRepository extends JpaRepository<CertificationEntity, Long> {

    @Query("select a from CertificationEntity a where a.entityRole = ?1")
    CertificationEntity findAdmin(EntityRole role);

    CertificationEntity findByEmail(String email);

    @Query("select c from CertificationEntity c where c.entityRole in (?1,?2)")
    List<CertificationEntity> findAllIssuers(EntityRole root,EntityRole subsystem);
}
