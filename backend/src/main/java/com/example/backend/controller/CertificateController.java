package com.example.backend.controller;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.service.interfaces.CertificateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.cert.Certificate;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;

    @GetMapping("/")
    public String Test(){
        certificateService.test();
        return "123";
    }

    @PostMapping()
    public ResponseEntity<Void> saveCertificate(@RequestBody CreationCertificateDto dto){
        if(!certificateService.saveCertificate(dto)) return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable Integer certificateId){
        Certificate certificate = certificateService.findCertificate(certificateId);
    }


}
