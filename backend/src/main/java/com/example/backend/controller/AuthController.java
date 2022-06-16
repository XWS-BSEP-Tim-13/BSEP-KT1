package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.model.CertificationEntity;
import com.example.backend.service.interfaces.AuthService;
import com.example.backend.service.interfaces.ForgotPasswordTokenService;
import com.example.backend.util.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    private final ForgotPasswordTokenService forgotPasswordTokenService;

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

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestBody String code) {
        authService.activateAccount(code);
        return new ResponseEntity<>("Account successfully activated!", HttpStatus.OK);
    }

    @GetMapping("forgot-password/mail/{email}")
    public ResponseEntity<Integer> forgotPassword(@PathVariable String email){
            try{
                Integer id=forgotPasswordTokenService.generateToken(email);
                return new ResponseEntity<>(id,HttpStatus.OK);
            }catch (EntityNotFoundException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found.");
            } catch (MessagingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while sending email!");
            } catch (UnsupportedEncodingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while sending email!");
            }
    }

    @GetMapping("/forgot-password/{token}")
    public ResponseEntity<Void> forgotPasswordRedirect(@PathVariable String token){
        try {
            String email=forgotPasswordTokenService.checkToken(token);
            URI frontend = new URI("http://localhost:3000/change-password/"+token);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(frontend);
            return new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token not found.");
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expired.");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDto dto) {
        try {
            authService.changePassword(dto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm password do not match.");
        }
    }

    @PostMapping("/generate-passwordless")
    public ResponseEntity<String> generatePasswordlessCode(@RequestBody PasswordlessCodeRequestDto codeRequest) {
        try{
            authService.generatePasswordlessCode(codeRequest);
        }
        catch (ResponseStatusException e){
            return new ResponseEntity<>("Certification entity with provided email does not exist.", HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Code successfully generated!", HttpStatus.OK);
    }

    @PostMapping("/passwordless-login")
    public ResponseEntity<UserTokenState> passwordlessLogin(@RequestBody PasswordlessLoginRequestDto loginRequestDto) {

        if(!authService.canUserLogInPasswordlessly(loginRequestDto)){
            return new ResponseEntity("Wrong email or code!", HttpStatus.BAD_REQUEST);
        }

        CertificationEntity user = authService.findByEmail(loginRequestDto.getEmail());
        String jwt = tokenUtils.generateToken(user.getEmail());
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getEmail(), user.getCommonName(), user.getRole().getAuthority(), user.getOrganization()));
    }
}
