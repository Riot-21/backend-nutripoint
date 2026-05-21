package com.example.backend_nutripoint.auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.backend_nutripoint.models.Compra;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendCodeRecoveryPassword(String to, String code){
        try {
            Context context = new Context();
            context.setVariable("code", code);
            String html = templateEngine.process("email-recovery-password", context);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Código de recuperación de contraseña");
            helper.setText(html, true); // TRUE = HTML

            emailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando email", e);
        }
        
    }

    public void enviarEmailCompra(Compra compra) {
        try {
            Context context = new Context();
            context.setVariable("usuario", compra.getUsuario().getEmail());
            context.setVariable("total", compra.getTotal());
            context.setVariable("detalles", compra.getDetalles());

            String html = templateEngine.process("email-compra", context);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(compra.getUsuario().getEmail());
            helper.setSubject("Confirmación de compra");
            helper.setText(html, true); // TRUE = HTML

            emailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }
}
