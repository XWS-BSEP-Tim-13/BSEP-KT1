package com.example.backend.model;

import com.example.backend.enums.CertificateStatus;
import com.example.backend.enums.CertificateType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="subject")
    @JsonIgnoreProperties("certificates")
    private CertificationEntity subject;

    @JsonIgnoreProperties("parent_certificate")
    @ManyToOne
    @JoinColumn(name="parent_certificate")
    private Certificate parentCertificate;

    @Column(name="valid_from")
    private Date validFrom;

    @Column(name="expiring_date")
    private Date expiringDate;

    @Column(name = "cerfilename")
    private String cerFileName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "certificate_purposes", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "purposes")
    private List<String> purposes = new ArrayList<>();

    private CertificateType type;

    @Column(name = "isCA", nullable = false)
    private boolean isCA;

    private String alias;

    @Transient
    private PrivateKey privateKey;

    @Column(name = "public_key")
    private PublicKey publicKey;

    @Column(name = "is_valid")
    private CertificateStatus certificateStatus = CertificateStatus.GOOD;
}
