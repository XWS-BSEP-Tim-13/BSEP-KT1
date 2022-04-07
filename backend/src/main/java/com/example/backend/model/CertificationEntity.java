package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationEntity extends BaseEntity{

    @Column(nullable = false)
    private String commonName;

    @Column(name="email", unique = true, nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="salt", nullable = false)
    private String salt;

    @OneToMany(mappedBy = "subject")
    private List<Certificate> certificates;

    private String organization;

    private String organizationUnit;

    private String countryCode;

    private boolean isSubsystem;

    @Transient
    private PrivateKey privateKey;

    @Column(nullable = false)
    private PublicKey publicKey;
}
