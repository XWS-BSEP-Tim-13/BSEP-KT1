package com.example.backend.repository;

import com.example.backend.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CertificationRepostory extends JpaRepository<Certificate, Integer> {
    @Query("select c from Certificate c where c.parentCertificate.id = ?1")
    List<Certificate> findCertificatesSignedByIssuer(Integer issuerId);
    Certificate findByAlias(String alias);

    @Query("select c from Certificate c where c.subject.id = ?1")
    List<Certificate> findBySubjectId(Integer subjectId);

    @Query("select c from Certificate c where c.subject.organization = ?1")
    List<Certificate> findByOrganization(String organization);

}
