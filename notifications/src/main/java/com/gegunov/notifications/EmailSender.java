package com.gegunov.notifications;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailSender {

    @Value("${mail.password}")
    private String password;
    @Value("${mail.username}")
    private String username;

    private Properties getProps() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtps.ssl.checkserveridentity", true);
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.ssl.enable", "true");
        return props;
    }

    private Session getSession() {
        return Session.getInstance(getProps(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void send(String to, String subject, String body) throws Exception {
        Message message = new MimeMessage(getSession());
        message.setFrom(new InternetAddress("tube90@mail.ru"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("egunov.g.a@gmail.com"));
        message.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }

}
