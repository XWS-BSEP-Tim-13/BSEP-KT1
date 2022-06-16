package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.enums.CertificateType;
import com.example.backend.model.Certificate;
import com.example.backend.dto.FetchCertificateDTO;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.service.interfaces.FetchCertificateService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.openssl.PEMWriter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import java.util.List;

import static com.example.backend.BackendApplication.LOGGER_INFO;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private Environment environment;
    private final FetchCertificateService fetchCertificateService;


    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<Void> saveCertificate(@RequestBody CreationCertificateDto dto){
        if(!certificateService.saveCertificate(dto)) return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        LOGGER_INFO.info("User: " + dto.getSubjectEntityId() + " | Action: CC");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("findAllByType/{type}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<List<CertificateBasicDto>> findAllByType(@PathVariable Integer type){
        LOGGER_INFO.info("Action: c/:type");
        return new ResponseEntity<>(certificateService.findAllByType(CertificateType.values()[type]),HttpStatus.OK);
    }

    @GetMapping("findById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<CertificateDto> findById(@PathVariable Integer id){
        LOGGER_INFO.info("Action: c/:id");
        return new ResponseEntity<>(certificateService.findCertificateInfo(id),HttpStatus.OK);
    }

    @GetMapping("/download/{certificateId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
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
        LOGGER_INFO.info("User: " + certificate.getSubjectDN().getName() + " | Action: DC");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> revokeCertificate(@PathVariable("id") Integer id){
        certificateService.revokeCertificate(id);
        LOGGER_INFO.info("User: " + principal.getName() + " | Action: RC");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/is-revoked/{id}")
    public ResponseEntity<Boolean> isCertificateRevoked(@PathVariable("id") Integer id, Principal principal){
        LOGGER_INFO.info("User: " + principal.getName() + " | Action: iCR/:id");
        return new ResponseEntity<>(certificateService.isCertificateRevoked(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<List<FetchCertificateDTO>> getAll(){
        LOGGER_INFO.info("Action: GC");
        return new ResponseEntity<>(fetchCertificateService.getAllCertificates(), HttpStatus.OK);
    }

    @GetMapping("/organization")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<List<FetchCertificateDTO>> getByOrganization(@RequestParam("organization") String organization){
        LOGGER_INFO.info("Action: c/:org");
        return new ResponseEntity<>(fetchCertificateService.getAllCertificatesByOrganization(organization), HttpStatus.OK);
    }

    @GetMapping("/subject/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<List<FetchCertificateDTO>> getBySubject(@PathVariable Integer id){
        LOGGER_INFO.info("Action: c/:subj");
        return new ResponseEntity<>(fetchCertificateService.getAllCertificatesBySubject(id), HttpStatus.OK);
    }

    @GetMapping("/hierarchy-above/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<List<FetchCertificateDTO>> getHierarchyAbove(@PathVariable Integer id){
        LOGGER_INFO.info("Action: HaC");
        return new ResponseEntity<>(fetchCertificateService.getHierarchyAbove(id), HttpStatus.OK);
    }

}
