package com.example.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateData extends BaseEntity{

    @Column(name = "publickey", nullable = false, length = 2048)
    private String publicKey;
    @Column(name = "keyalgorithm", nullable = false)
    private String keyAlgorithm;
    @Column(name = "commonname", nullable = false)
    private String commonName;

    @Column(name = "givenname")
    private String givenName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "organization")
    private String organization;

    @Column(name = "organizationalunit")
    private String organizationalUnit;

    @Column(name = "countrycode")
    private String countryCode;

    @Column(name = "emailaddress")
    private String emailAddress;

    @Column(name = "serialNumber")
    private int serialNumber;

    @Column(name = "isCA", nullable = false)
    private boolean isCA;

    @Column(name = "subjectkeyidentifier")
    private String subjectKeyIdentifier;
}
