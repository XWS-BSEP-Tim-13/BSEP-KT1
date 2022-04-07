package com.example.backend.model;

import com.example.backend.enums.CertificateType;
import lombok.*;

import javax.persistence.*;
import java.security.PublicKey;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="subject")
    private CertificationEntity subject;

    @ManyToOne
    @JoinColumn(name="parent_certificate")
    private Certificate parentCertificate;

    @Column(name="issuing_date")
    private Date issuingDate;

    @Column(name="expiring_date")
    private Date expiringDate;

    @Column(name = "cerfilename")
    private String cerFileName;

    @Column(nullable = false)
    private PublicKey publicKey;

    @Column(name="serial_number",nullable = false,unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serialNumber;

    private String purpose;

    private CertificateType type;

    @Column(name = "isCA", nullable = false)
    private boolean isCA;
}
