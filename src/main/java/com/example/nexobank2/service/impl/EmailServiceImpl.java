package com.example.nexobank2.service.impl;

import com.example.nexobank2.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String mailName;

    private JavaMailSender mailSender;
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Override
    public void sendSimpleMessage(String to, String subject, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailName);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(token);
        mailSender.send(message);
    }
}
