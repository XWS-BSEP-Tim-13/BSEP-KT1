package com.example.backend.model;

import com.example.backend.enums.EntityRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "subject")
    private List<Certificate> certificates = new ArrayList<>();

    private String organization;

    private String organizationUnit;

    private String countryCode;

    private EntityRole entityRole;

    private String alias;

    @Transient
    private PrivateKey privateKey;

    @Column(nullable = false)
    private PublicKey publicKey;
}
