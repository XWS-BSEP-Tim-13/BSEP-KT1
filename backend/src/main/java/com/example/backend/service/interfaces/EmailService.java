package com.example.backend.service.interfaces;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
