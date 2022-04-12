package com.example.backend.controller;

import com.example.backend.dto.CertificateBasicDto;
import com.example.backend.dto.CertificateDto;
import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.enums.CertificateType;
import com.example.backend.model.Certificate;
import com.example.backend.service.interfaces.CertificateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;
    private Environment environment;

    @PostMapping()
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
    public ResponseEntity<Void> downloadCertificate(@PathVariable Integer certificateId){
        X509Certificate certificate = certificateService.findCertificate(certificateId);
        Certificate dbCert = certificateService.findDbCert(certificateId);
        try{
            String path=environment.getProperty("certificate.folder");
            final FileOutputStream os = new FileOutputStream(path+dbCert.getSubject().getCommonName()+".cer");
            os.write("-----BEGIN CERTIFICATE-----\n".getBytes("US-ASCII"));
            os.write(Base64.encodeBase64(certificate.getEncoded(), true));
            os.write("-----END CERTIFICATE-----\n".getBytes("US-ASCII"));
            os.close();
            File file = new File(path+dbCert.getSubject().getCommonName()+".pem");
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


}
