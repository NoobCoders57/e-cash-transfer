package org.example.util.interfaces;

import javax.mail.MessagingException;

public interface MailSender {
    void send(String subject, String message, String recipient) throws MessagingException;
}
