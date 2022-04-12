package com.example.backend.controller;

import com.example.backend.dto.CertificateBasicDto;
import com.example.backend.dto.CertificateDto;
import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateType;
import com.example.backend.model.Certificate;
import com.example.backend.dto.FetchCertificateDTO;
import com.example.backend.dto.NewCertificateSubjectDTO;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.service.interfaces.FetchCertificateService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.openssl.PEMWriter;
import org.springframework.core.env.Environment;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import java.util.List;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private Environment environment;
    private final FetchCertificateService fetchCertificateService;


    @PostMapping("/")
    public ResponseEntity<Void> saveCertificate(@RequestBody CreationCertificateDto dto){
        if(!certificateService.saveCertificate(dto)) return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("findAllByType/{type}")
    public ResponseEntity<List<CertificateBasicDto>> findAllByType(@PathVariable Integer type){
        return new ResponseEntity<>(certificateService.findAllByType(CertificateType.values()[type]),HttpStatus.OK);
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<CertificateDto> findById(@PathVariable Integer id){
        return new ResponseEntity<>(certificateService.findCertificateInfo(id),HttpStatus.OK);
    }

    @GetMapping("/download/{certificateId}")
    public ResponseEntity<Void> downloadCertificate(@PathVariable Integer certificateId) {
        X509Certificate certificate = certificateService.findCertificate(certificateId);
        Certificate dbCert = certificateService.findDbCert(certificateId);
        try {
            String path = environment.getProperty("certificate.folder");
            final FileOutputStream os = new FileOutputStream(path + dbCert.getSubject().getCommonName() + ".cer");
            os.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
            os.write(Base64.encodeBase64(certificate.getEncoded(), true));
            os.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
            os.close();
            File file = new File(path + dbCert.getSubject().getCommonName() + ".pem");
            PEMWriter pubWriter = new PEMWriter(new FileWriter(file));
            pubWriter.writeObject(certificate);
            pubWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> revokeCertificate(@PathVariable("id") Integer id){
        certificateService.revokeCertificate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FetchCertificateDTO>> getAll(){
        return new ResponseEntity<>(fetchCertificateService.getAllCertificates(), HttpStatus.OK);
    }

    @GetMapping("/organization")
    public ResponseEntity<List<FetchCertificateDTO>> getByOrganization(@RequestParam("organization") String organization){
        return new ResponseEntity<>(fetchCertificateService.getAllCertificatesByOrganization(organization), HttpStatus.OK);
    }

    @GetMapping("/subject/{id}")
    public ResponseEntity<List<FetchCertificateDTO>> getBySubject(@PathVariable Integer id){
        return new ResponseEntity<>(fetchCertificateService.getAllCertificatesBySubject(id), HttpStatus.OK);
    }

    @GetMapping("/subjects")
    public ResponseEntity<Set<NewCertificateSubjectDTO>> getPossibleSubjectsForNewCertificate() {
        Set<NewCertificateSubjectDTO> subjects = certificateService.getPossibleSubjectsForNewCertificate();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }
    @GetMapping("/hierarchy-above/{id}")
    public ResponseEntity<List<FetchCertificateDTO>> getHierarchyAbove(@PathVariable Integer id){
        return new ResponseEntity<>(fetchCertificateService.getHierarchyAbove(id), HttpStatus.OK);
    }


}
