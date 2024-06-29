package org.example.util.mail;

import org.example.models.Client;
import org.example.models.Envoyer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Build the message to be sent by email
 */
public class MessageBuilder {
    /**
     * Type of message to be sent
     */
    public enum MessageType {
        CASH_SENT,
        CASH_RECEIVED
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
     * @param transaction Transaction data
     * @param sender Sender of the transaction
     * @param receiver Receiver of the transaction
     */
    public MessageBuilder(@NotNull Envoyer transaction, Client sender, Client receiver) {
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

    /**
     * Build the message to be sent
     * @param type Type of message
     * @param template path to the template file
     * @return The message to be sent
     * @throws IOException If the template file could not be loaded
     */
    public String buildMessage(MessageType type, String template) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String message;
        try (InputStream inputStream = loader.getResourceAsStream(template)) {
            if (inputStream != null) {
                byte[] bytes = inputStream.readAllBytes();
                message = new String(bytes);
            } else throw new RuntimeException("Could not load the template file" + template);
        }

        for (String placeholder : MESSAGE_VARS) {
            message = message.replace("${" + placeholder + "}", data.get(type).get(placeholder));
        }

        return message;
    }
}
