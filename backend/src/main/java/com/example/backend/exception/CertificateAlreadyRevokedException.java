package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This certificate is already revoked!")
public class CertificateAlreadyRevokedException extends RuntimeException{
}
