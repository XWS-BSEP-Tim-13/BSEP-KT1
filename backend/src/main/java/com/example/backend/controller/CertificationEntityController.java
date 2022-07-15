package com.example.backend.controller;

import com.example.backend.dto.CertificateIssuerDTO;
import com.example.backend.dto.NewCertificateSubjectDTO;
import com.example.backend.service.interfaces.CertificationEntityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static com.example.backend.BackendApplication.LOGGER_INFO;

@RestController
@RequestMapping("/certification-entity")
@AllArgsConstructor
public class CertificationEntityController {

    private final CertificationEntityService certificationEntityService;

    @GetMapping("/subjects")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<Set<NewCertificateSubjectDTO>> getPossibleSubjectsForNewCertificate() {
        Set<NewCertificateSubjectDTO> subjects = certificationEntityService.getPossibleSubjectsForNewCertificate();
        LOGGER_INFO.info("Action: GPI");
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/issuers/{organization}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_SUBSYSTEM')")
    public ResponseEntity<List<CertificateIssuerDTO>> getIssuersByOrganization(@PathVariable("organization") String organization){
        LOGGER_INFO.info("User: " + organization + " | Action: I/:org");
        return new ResponseEntity<>(certificationEntityService.findSuitableIssuersForCertificateSigning(organization), HttpStatus.OK);
    }
}
