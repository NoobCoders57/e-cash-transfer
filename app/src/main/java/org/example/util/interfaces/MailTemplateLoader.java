package org.example.util.interfaces;

import org.example.util.mail.MessageBuilder;

import java.io.IOException;

/**
 * Functional interface that provides the template to be used
 */
public interface MailTemplateLoader {
    /**
     * Load the template for the given message type
     *
     * @param type Type of message to be sent
     * @return The template for the given message type
     * @throws IOException If an I/O error occurs
     */
    String loadTemplate(MessageBuilder.MessageType type) throws IOException;
}
