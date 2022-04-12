package com.example.backend.controller;

import com.example.backend.dto.CreationCertificateDto;
import com.example.backend.dto.FetchCertificateDTO;
import com.example.backend.service.interfaces.CertificateService;
import com.example.backend.service.interfaces.FetchCertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificate")
@AllArgsConstructor
public class CertificateController {


    private final CertificateService certificateService;
    private final FetchCertificateService fetchCertificateService;

    @GetMapping("/")
    public String Test(){
        return "123";
    }

    @PostMapping()
    public ResponseEntity<Void> saveCertificate(@RequestBody CreationCertificateDto dto){
        if(!certificateService.saveCertificate(dto)) return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FetchCertificateDTO>> getAll(){
        return new ResponseEntity<>(fetchCertificateService.getAllCertificates(), HttpStatus.OK);
    }

    @GetMapping("/organization")
    public ResponseEntity<List<FetchCertificateDTO>> getByOrganization(@RequestParam("organization") String organization){
        return new ResponseEntity<>(fetchCertificateService.getAllCertificatesByOrganization(organization), HttpStatus.OK);
    }

    @GetMapping("/subject")
    public ResponseEntity<List<FetchCertificateDTO>> getBySubject(@RequestParam("id") Integer subjectId){
        return new ResponseEntity<>(fetchCertificateService.getAllCertificatesBySubject(subjectId), HttpStatus.OK);
    }


}
