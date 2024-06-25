package org.example.util;

import org.example.models.Envoyer;

/**
 * Notification of the two parties involved in the transaction by e-mail
 */
public class MailNotifier {
    private final Envoyer transaction;

    public MailNotifier(Envoyer transaction) {
        this.transaction = transaction;
    }
    /**
     * Email the cash receiver
     */
    public void notifyReceiver() {
        // Send email to the receiver
    }

    public void notifySender() {
        // Send email to the sender
    }

    private String buildReceiverMessage() {
        String message = "";
        // Init the message here: read and fill the template
        return message;
    }

    private String buildSenderMessage() {
        String message = "";
        // Init the message here: read and fill the template
        return message;
    }
}
