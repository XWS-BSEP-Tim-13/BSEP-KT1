package com.example.backend;

import org.slf4j.LoggerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class BackendApplication {

	public static final org.slf4j.Logger LOGGER_INFO= LoggerFactory.getLogger("pki-info");
	public static final org.slf4j.Logger LOGGER_ERROR= LoggerFactory.getLogger("pki-error");
	public static final org.slf4j.Logger LOGGER_WARNING= LoggerFactory.getLogger("pki-warning");

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		Security.addProvider(new BouncyCastleProvider());
	}

}
