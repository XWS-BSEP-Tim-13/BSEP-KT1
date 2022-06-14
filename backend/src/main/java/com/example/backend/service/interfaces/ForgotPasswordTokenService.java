package com.example.backend.service.interfaces;

import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Repository
public interface ForgotPasswordTokenService {

    Integer generateToken(String username) throws MessagingException, UnsupportedEncodingException;
    String checkToken(String token) throws Exception;
}
