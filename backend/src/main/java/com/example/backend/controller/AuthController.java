package com.example.backend.controller;

import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.model.CertificationEntity;
import com.example.backend.service.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CertificationEntity> registerCertificationEntity(@RequestBody RegistrationEntityDTO registrationEntity){
        CertificationEntity entity = authService.registerCertificationEntity(registrationEntity);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }
}
