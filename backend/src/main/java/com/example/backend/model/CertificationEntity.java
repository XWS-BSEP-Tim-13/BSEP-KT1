package com.example.backend.model;

import com.example.backend.enums.EntityRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "subject",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("subject")
    private List<Certificate> certificates = new ArrayList<>();

    private String organization;

    private String organizationUnit;

    private String countryCode;

    private EntityRole entityRole;

    @Transient
    private PrivateKey privateKey;

    @Column()
    private PublicKey publicKey;
}
