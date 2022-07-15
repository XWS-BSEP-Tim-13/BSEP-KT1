package com.example.backend.service.interfaces;

import com.example.backend.email_feedback.Mail;
import com.example.backend.model.PasswordlessCredentials;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendMail(MimeMessage mimeMessage, Mail mail) throws MessagingException, UnsupportedEncodingException;
    void sendPasswordlessCode(PasswordlessCredentials credentials) throws MessagingException, UnsupportedEncodingException;
}
