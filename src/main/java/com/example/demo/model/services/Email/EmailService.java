package com.example.demo.model.services.Email;

import com.example.demo.model.DTOs.MailDTO;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    @Value("${MAIL}")
    private String mail;
    private final JavaMailSender mailSender;
    private final UsuarioRepository usuarioRepository;

    public EmailService(JavaMailSender mailSender, UsuarioRepository usuarioRepository) {
        this.mailSender = mailSender;
        this.usuarioRepository = usuarioRepository;
    }

    //preguntar si seria correcto que sea String[] y no list ya que no sabemos a cuantos usuarios podemos escribir
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void SendMailToAll(MailDTO mailDTO) {
        List<UsuarioEntity> users = usuarioRepository.findAllByActivo(true);

        users.forEach(p -> sendEmail(p.getEmail(), mailDTO.getSubject(), mailDTO.getMensaje()));
    }

    public void recibirEmail(MailDTO dto, String nombre, String mailEmisor) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        //MimeMessage es una version mas completa de SimpleMessagge
        //el replyTo es para cuando nos llega un msj y tocamos responder, le respondemos al usuario que lo envio
        //hace falta ya que si no, nos estariamos mandando el mensaje a nosotros mismos

        helper.setTo(mail);
        helper.setSubject(dto.getSubject());
        //aclaramos el mail y nombre del user ya que en realidad esto es un correo de nosotros hacia nosotros mismos
        helper.setText("Nombre: " + nombre + "\nEmail: " + mailEmisor + "\n" + dto.getMensaje());
        helper.setReplyTo(mailEmisor);

        mailSender.send(mimeMessage);
    }

}
