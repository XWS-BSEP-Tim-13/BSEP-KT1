package com.example.backend.controller;

import com.example.backend.service.interfaces.CertificateService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
