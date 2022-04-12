package com.example.backend.controller;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.service.interfaces.CertificateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;

    @GetMapping("/")
    public String Test(){
        return "123";
    }

    @PostMapping()
    public ResponseEntity<Void> saveCertificate(@RequestBody CreationCertificateDto dto){
        if(!certificateService.saveCertificate(dto)) return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> revokeCertificate(@PathVariable("id") Integer id){
        certificateService.revokeCertificate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
