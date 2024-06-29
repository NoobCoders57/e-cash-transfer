package org.example.util.interfaces;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface TransportProvider {
    void send(MimeMessage message) throws MessagingException;
}
