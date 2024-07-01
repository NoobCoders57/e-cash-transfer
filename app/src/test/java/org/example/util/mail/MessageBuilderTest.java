package org.example.util.mail;

import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.interfaces.MailTemplateLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MessageBuilderTest {
    @Mock
    private MailTemplateLoader templateLoader;

    private AutoCloseable mocks;
    private Client sender;
    private Client receiver;
    private Envoyer transaction;
    private Date date;
    String unite = "Ariary";

    @BeforeEach
    public void setup() {
        mocks = MockitoAnnotations.openMocks(this);
        date = new Date();
        sender = new Client("123", "Sender", "Male", "Antananarivo", 1000, "test@site.com");
        receiver = new Client("456", "Receiver", "Male", "Antananarivo", 10000, "test2@site.com");
        transaction = new Envoyer(sender.numtel(), receiver.numtel(), 400, date, "Test");
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldBuildCorrectCashReceivedMessage() throws IOException, SQLException {
        Envoyer transaction = new Envoyer(sender.numtel(), receiver.numtel(), 400, new Date(), "Test");
        when(templateLoader.loadTemplate(MessageBuilder.MessageType.CASH_RECEIVED)).thenReturn("${nom} ${num} ${montant} ${unite} ${date} ${raison}");
        MessageBuilder messageBuilder = new MessageBuilder(transaction, sender, receiver, templateLoader);
        String message = messageBuilder.buildMessage(MessageBuilder.MessageType.CASH_RECEIVED);

        assertEquals(
                "%s %s %s %s %s %s".formatted(
                        sender.nom(), transaction.numEnvoyeur(), transaction.montant(), unite, date, transaction.raison()
                ),
                message
        );
    }

    @Test
    public void shouldBuildCorrectCashSentMessage() throws IOException, SQLException {
        when(templateLoader.loadTemplate(MessageBuilder.MessageType.CASH_SENT)).thenReturn("${nom} ${num} ${montant} ${unite} ${date} ${raison}");
        MessageBuilder messageBuilder = new MessageBuilder(transaction, sender, receiver, templateLoader);
        String message = messageBuilder.buildMessage(MessageBuilder.MessageType.CASH_SENT);

        assertEquals(
                "%s %s %s %s %s %s".formatted(
                        receiver.nom(), transaction.numRecepteur(), transaction.montant(), unite, date, transaction.raison()
                ),
                message
        );
    }

    @Test
    public void shouldThrowExceptionWhenTemplateNotFound() throws IOException, SQLException {
        when(templateLoader.loadTemplate(MessageBuilder.MessageType.CASH_SENT)).thenThrow(new RuntimeException("Could not load the template file"));
        MessageBuilder messageBuilder = new MessageBuilder(transaction, sender, receiver, templateLoader);

        assertThrows(RuntimeException.class, () -> messageBuilder.buildMessage(MessageBuilder.MessageType.CASH_SENT));
    }

    @Nested
    class DefaultMailTemplateLoaderTest {
        @Test
        public void shouldLoadCorrectTemplate() throws IOException {
            MessageBuilder.DefaultMailTemplateLoader loader = new MessageBuilder.DefaultMailTemplateLoader();

            String cashReceivedTemplate = loader.loadTemplate(MessageBuilder.MessageType.CASH_RECEIVED);
            String cashSentTemplate = loader.loadTemplate(MessageBuilder.MessageType.CASH_SENT);

            assertNotNull(cashReceivedTemplate);
            assertNotNull(cashSentTemplate);
        }
    }
}