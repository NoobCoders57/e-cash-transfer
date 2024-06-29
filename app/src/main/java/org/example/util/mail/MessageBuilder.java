package org.example.util.mail;

import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.interfaces.MailTemplateLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Build the message to be sent by email
 */
public class MessageBuilder {
    private final MailTemplateLoader templateLoader;

    /**
     * Type of message to be sent
     */
    public enum MessageType {
        CASH_SENT("cash-sent"),
        CASH_RECEIVED("cash-received");

        private final String value;

        MessageType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    private final String[] MESSAGE_VARS = {
            "montant",
            "unite",
            "nom",
            "num",
            "date",
            "raison"
    };
    private final HashMap<MessageType, HashMap<String, String>> data = new HashMap<>();

    /**
     * Constructor
     *
     * @param transaction    Transaction to be notified
     * @param sender         Sender of the transaction
     * @param receiver       Receiver of the transaction
     * @param templateLoader A functional interface that provides the template to be used
     */
    public MessageBuilder(@NotNull Envoyer transaction, Client sender, Client receiver, MailTemplateLoader templateLoader) {
        this.templateLoader = templateLoader;

        HashMap<String, String> receiverData = new HashMap<>();
        receiverData.put("montant", String.valueOf(transaction.montant()));
        receiverData.put("unite", "Ariary");
        receiverData.put("nom", receiver.nom());
        receiverData.put("num", transaction.numRecepteur());
        receiverData.put("date", transaction.date().toString());
        receiverData.put("raison", transaction.raison());
        data.put(MessageType.CASH_RECEIVED, receiverData);

        HashMap<String, String> senderData = new HashMap<>();
        senderData.put("montant", String.valueOf(transaction.montant()));
        senderData.put("unite", "Ariary");
        senderData.put("nom", sender.nom());
        senderData.put("num", transaction.numEnvoyeur());
        senderData.put("date", transaction.date().toString());
        senderData.put("raison", transaction.raison());
        data.put(MessageType.CASH_SENT, senderData);
    }

    public MessageBuilder(@NotNull Envoyer transaction, Client sender, Client receiver) {
        this(transaction, sender, receiver, new DefaultMailTemplateLoader());
    }

    /**
     * Build the message to be sent, using the template loader provided in the constructor
     *
     * @param type     Type of message
     * @return The message to be sent
     * @throws IOException If the template file could not be loaded
     */
    public String buildMessage(MessageType type) throws IOException {
        String message = templateLoader.loadTemplate(type);

        for (String placeholder : MESSAGE_VARS) {
            message = message.replace("${" + placeholder + "}", data.get(type).get(placeholder));
        }

        return message;
    }

    public static class DefaultMailTemplateLoader implements MailTemplateLoader {
        @Override
        public String loadTemplate(MessageType type) throws IOException {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String template = "templates/mail/%s.html".formatted(type.value());
            try (InputStream inputStream = loader.getResourceAsStream(template)) {
                if (inputStream != null) {
                    byte[] bytes = inputStream.readAllBytes();
                    return new String(bytes);
                } else throw new RuntimeException("Could not load the template file" + template);
            }
        }
    }
}
