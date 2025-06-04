package com.example.demo.model.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${MAIL}")
    private String email;
    @Value("${MAIL_PASSWORD}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        //tipo de mail
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        //definimos protocolo
        props.put("mail.transport.protocol", "smtp");
        //habilitamos la autenticacion del protocolo
        props.put("mail.smtp.auth", "true");
        //ciframos la comunicacion
        props.put("mail.smtp.starttls.enable", "true");
        //una vez que acaben las pruebas desactivar(es para mostrar en consola los estados)
        props.put("mail.debug", "true");

        return mailSender;
    }
}
