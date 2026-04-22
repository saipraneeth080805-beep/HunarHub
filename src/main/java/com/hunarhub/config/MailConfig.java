package com.hunarhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        // Uses Spring Boot mail properties if you set them in application.properties.
        // For now this is a simple sender; configure host/port/username/password there.
        return new JavaMailSenderImpl();
    }
}

