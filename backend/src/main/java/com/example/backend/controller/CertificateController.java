package com.example.backend.controller;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.dto.NewCertificateSubjectDTO;
import com.example.backend.service.interfaces.CertificateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/")
    public String Test(){
        return "123";
    }

    @PostMapping("/")
    public ResponseEntity<Void> saveCertificate(@RequestBody CreationCertificateDto dto){
        if(!certificateService.saveCertificate(dto)) return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/subjects")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<NewCertificateSubjectDTO>> getPossibleSubjectsForNewCertificate() {
        Set<NewCertificateSubjectDTO> subjects = certificateService.getPossibleSubjectsForNewCertificate();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }


}
