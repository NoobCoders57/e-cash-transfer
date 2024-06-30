package org.example.util.mail;

import org.example.dao.ClientDao;
import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.interfaces.ClientProvider;
import org.example.util.interfaces.MailCompleteListener;
import org.example.util.interfaces.MailSender;
import org.example.util.interfaces.ObservableMailNotifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Notification of the two parties involved in the transaction by e-mail
 */
public class MailNotifier implements ObservableMailNotifier {
    private final MailSender mailer;

    /**
     * Parties involved in the transaction
     */
    public enum Party {
        CASH_SENDER("sender"),
        CASH_RECEIVER("receiver");

        private final String value;

        Party(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    private final Envoyer transaction;
    private final HashMap<Party, Client> clients = new HashMap<>();
    private final HashMap<EventType, MailCompleteListener> mailCompleteListener = new HashMap<>();
    private static final HashMap<Party, MessageBuilder.MessageType> messageTypeMapping = new HashMap<>();

    static {
        messageTypeMapping.put(Party.CASH_SENDER, MessageBuilder.MessageType.CASH_SENT);
        messageTypeMapping.put(Party.CASH_RECEIVER, MessageBuilder.MessageType.CASH_RECEIVED);
    }

    /**
     * Create a new MailNotifier with the given transaction, client provider and mailer
     *
     * @param transaction    Transaction to be notified
     * @param clientProvider Provider of clients
     * @param mailer         Mailer to send emails
     * @throws SQLException If a database access error occurs
     */
    public MailNotifier(@NotNull Envoyer transaction, @Nullable ClientProvider clientProvider, @Nullable MailSender mailer) throws SQLException {
        this.transaction = transaction;
        this.mailer = mailer != null ? mailer : new Mailer();
        ClientProvider provider = clientProvider != null ? clientProvider : numtel -> new ClientDao().getClient(numtel);
        this.clients.put(Party.CASH_RECEIVER, provider.getClient(transaction.numRecepteur()));
        this.clients.put(Party.CASH_SENDER, provider.getClient(transaction.numEnvoyeur()));
    }

    /**
     * Create a new MailNotifier with the given transaction a default client provider and a default mailer
     *
     * @param transaction Transaction to be notified
     * @throws SQLException If a database access error occurs
     */
    public MailNotifier(@NotNull Envoyer transaction) throws SQLException {
        this(transaction, null, null);
    }

    /**
     * Email one of parties involved in the transaction in a separate thread
     * Notify the listeners when the email sending is complete (whether it was successful or not)
     *
     * @param party Party to be notified
     */
    public void notify(Party party) {
        MessageBuilder.MessageType messageType = messageTypeMapping.get(party);
        new Thread(() -> {
            String mail = clients.get(party).mail();
            try {
                String message = new MessageBuilder(
                        transaction,
                        clients.get(Party.CASH_SENDER),
                        clients.get(Party.CASH_RECEIVER)
                ).buildMessage(messageType);
                mailer.send("Rapport de transaction", message, mail);
                notifySendListener(EventType.SEND_SUCCESS, "Email sent to " + mail);
                Logger.getLogger("MailNotifier").info("Email sent to " + mail);
            } catch (MessagingException | IOException e) {
                Logger.getLogger("MailNotifier").severe("Sending email failed: " + e.getMessage());
                notifySendListener(EventType.SEND_FAILURE, "Failed to send email to " + mail);
            }
        }).start();
    }

    /**
     * @param type    Type of event
     * @param message Description of the event
     */
    @Override
    public void notifySendListener(EventType type, String message) {
        MailCompleteListener listener = mailCompleteListener.get(type);
        if (listener != null) {
            listener.onComplete(message);
        }
    }

    /**
     * @param type     Type of event
     * @param listener Listener to be notified
     */
    @Override
    public void setSendListener(EventType type, @NotNull MailCompleteListener listener) {
        if (type != null) {
            mailCompleteListener.put(type, listener);
        }
    }

    /**
     * @param type Type of event
     * @return Listener to be notified when the event is triggered
     */
    @Override
    public MailCompleteListener getSendListener(EventType type) {
        return type != null? mailCompleteListener.get(type): null;
    }

    /**
     * @param type Type of event
     */
    @Override
    public void removeSendListener(@NotNull EventType type) {
        mailCompleteListener.remove(type);
    }
}
