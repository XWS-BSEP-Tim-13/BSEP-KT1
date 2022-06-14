package com.example.backend.controller;

import com.example.backend.dto.JwtAuthenticationRequest;
import com.example.backend.dto.RegistrationEntityDTO;
import com.example.backend.dto.UserTokenState;
import com.example.backend.model.CertificationEntity;
import com.example.backend.service.interfaces.AuthService;
import com.example.backend.util.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        Authentication authentication=null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        }
        catch (DisabledException disabledException){
            return new ResponseEntity("User not enabled!",HttpStatus.BAD_REQUEST);
        }
        catch (Exception ex){
            return new ResponseEntity("Wrong email or password!",HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CertificationEntity user = (CertificationEntity) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getEmail());
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getEmail(), user.getCommonName(), user.getRole().getAuthority(), user.getOrganization()));
    }


    @PostMapping("/register")
    public ResponseEntity<CertificationEntity> registerCertificationEntity(@RequestBody RegistrationEntityDTO registrationEntity){
        CertificationEntity entity = authService.registerCertificationEntity(registrationEntity);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> activateAccount(@RequestBody String code) {
        authService.activateAccount(code);
        return new ResponseEntity<>("Account successfully activated!", HttpStatus.OK);
    }
}
