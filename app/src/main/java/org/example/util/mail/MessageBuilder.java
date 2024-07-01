package org.example.util.mail;

import org.example.dao.FraisDao;
import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.interfaces.MailTemplateLoader;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;

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
            "raison",
            "frais"
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
    public MessageBuilder(@NotNull Envoyer transaction, Client sender, Client receiver, MailTemplateLoader templateLoader) throws SQLException {
        this.templateLoader = templateLoader;

        HashMap<String, String> receiverData = new HashMap<>();
        receiverData.put("montant", String.valueOf(transaction.montant()));
        receiverData.put("unite", "Ariary");
        receiverData.put("nom", receiver.nom());
        receiverData.put("num", transaction.numRecepteur());
        receiverData.put("date", transaction.date().toString());
        receiverData.put("raison", transaction.raison());
        receiverData.put("frais", String.valueOf(new FraisDao().getFraisValueForMontant(transaction.montant())));
        data.put(MessageType.CASH_SENT, receiverData);

        HashMap<String, String> senderData = new HashMap<>();
        senderData.put("montant", String.valueOf(transaction.montant()));
        senderData.put("unite", "Ariary");
        senderData.put("nom", sender.nom());
        senderData.put("num", transaction.numEnvoyeur());
        senderData.put("date", transaction.date().toString());
        senderData.put("raison", transaction.raison());
        senderData.put("frais", String.valueOf(new FraisDao().getFraisValueForMontant(transaction.montant())));
        data.put(MessageType.CASH_RECEIVED, senderData);
    }

    public MessageBuilder(@NotNull Envoyer transaction, Client sender, Client receiver) throws SQLException {
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
                    InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    return new BufferedReader(reader).lines().collect(Collectors.joining("\n"));
                } else throw new RuntimeException("Could not load the template file" + template);
            }
        }
    }
}
