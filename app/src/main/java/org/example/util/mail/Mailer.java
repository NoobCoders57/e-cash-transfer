package org.example.util.mail;


import org.example.util.config.Config;
import org.example.util.interfaces.MailSender;
import org.example.util.interfaces.TransportProvider;
import org.example.util.interfaces.SessionProvider;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mailer implements MailSender {
    private final TransportProvider transportProvider;
    private final SessionProvider sessionProvider;

    public Mailer(TransportProvider transportProvider, SessionProvider sessionProvider) {
        this.transportProvider = transportProvider;
        this.sessionProvider = sessionProvider;
    }

    public Mailer() {
        this(Transport::send, Session::getDefaultInstance);
    }

    @Override
    public void send(String subject, String strMessage, String recipient) throws MessagingException {
        Properties properties = getProperties();
        Session session = sessionProvider.getSession(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.get("mail.username"), Config.get("mail.password"));
            }
        });
        session.setDebug(true);
        session.setDebugOut(System.out);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Config.get("mail.from")));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setContent(strMessage, "text/html");
        message.saveChanges();
        transportProvider.send(message);
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.debug", "true");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.required", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return properties;
    }
}
