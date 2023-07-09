package com.example.datvexe.services;

import org.springframework.stereotype.Component;

@Component
public interface EmailService {
    void sendEmail(String email, String msg, String subject, boolean html);
}
