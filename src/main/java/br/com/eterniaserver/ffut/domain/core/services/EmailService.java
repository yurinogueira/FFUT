package br.com.eterniaserver.ffut.domain.core.services;

import br.com.eterniaserver.ffut.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.address}")
    private String fromEmailAddress;

    @Value("${spring.mail.enabled}")
    private Boolean enabled;

    @Async
    public void sendEmail(String recipient, String subject, String content) throws ResponseStatusException {
        if (!enabled) {
            return;
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmailAddress);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(message);
        } catch (MessagingException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Constants.EMAIL_SERVICE_ERROR);
        }
    }

}
