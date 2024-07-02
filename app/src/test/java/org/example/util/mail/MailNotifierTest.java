package org.example.util.mail;

import org.example.dao.ClientDao;
import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.ClientProvider;
import org.example.util.interfaces.MailCompleteListener;
import org.example.util.interfaces.MailSender;
import org.example.util.interfaces.ObservableMailNotifier.EventType;
import org.example.util.mail.MailNotifier.Party;
import org.example.util.providers.DefaultClientProvider;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.Date;

import static org.mockito.Mockito.*;

class MailNotifierTest {

    @Mock
    private Envoyer transaction;

    @Mock
    private ClientProvider clientProvider;

    @Mock
    private Client client;

    @Mock
    private MailCompleteListener listener;

    @Mock
    private MailSender mailer;

    AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Nested
    class NotifyTests {
        @BeforeEach
        void setUp() throws ModelProviderException {
            when(clientProvider.getClient(any())).thenReturn(client);
            when(client.mail()).thenReturn("test@example.com");
            when(client.nom()).thenReturn("Test");
            when(client.pays()).thenReturn("france");
            when(transaction.montant()).thenReturn(1000f);
            when(transaction.numEnvoyeur()).thenReturn("1234567890");
            when(transaction.numRecepteur()).thenReturn("0987654321");
            when(transaction.raison()).thenReturn("Test");
            when(transaction.date()).thenReturn(new Date());
        }

        @Test
        void shouldNotifyListenerOnSuccessfulEmailSend() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            notifier.setSendListener(EventType.SEND_SUCCESS, listener);
            notifier.notify(Party.CASH_SENDER);

            verify(listener, timeout(1000)).onComplete(anyString());
        }

        @Test
        void shouldNotifyListenerOnFailedEmailSend() throws MessagingException, ModelProviderException {
            doThrow(MessagingException.class).when(mailer).send(anyString(), anyString(), anyString());

            MailNotifier notifier = new MailNotifier(transaction, clientProvider, mailer);
            notifier.setSendListener(EventType.SEND_FAILURE, listener);
            notifier.notify(Party.CASH_SENDER);

            verify(listener, timeout(1000)).onComplete(anyString());
        }

        @Test
        void shouldNotNotifyListenerWhenRemoved() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            notifier.setSendListener(EventType.SEND_SUCCESS, listener);
            notifier.removeSendListener(EventType.SEND_SUCCESS);
            notifier.notify(Party.CASH_SENDER);

            verify(listener, after(1000).never()).onComplete(anyString());
        }
    }

    @Nested
    class NotifySendListenerTests {
        @Test
        void shouldNotifyListenerOnSuccessEvent() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            notifier.setSendListener(EventType.SEND_SUCCESS, listener);
            notifier.notifySendListener(EventType.SEND_SUCCESS, "Test message");
            verify(listener, timeout(1000)).onComplete("Test message");
        }

        @Test
        void shouldNotifyListenerOnFailureEvent() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            notifier.setSendListener(EventType.SEND_FAILURE, listener);
            notifier.notifySendListener(EventType.SEND_FAILURE, "Test message");
            verify(listener, timeout(1000)).onComplete("Test message");
        }
    }

    @Nested
    class SetSendListenerTests {
        @Test
        void shouldSetListenerForEventType() throws ModelProviderException {
            for (EventType eventType : EventType.values()) {
                MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
                notifier.setSendListener(eventType, listener);
                MailCompleteListener actualListener = notifier.getSendListener(eventType);

                assert actualListener == listener;
            }
        }

        @Test
        void shouldNotSetListenerForNullEventType() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            notifier.setSendListener(null, listener);
            MailCompleteListener actualListener = notifier.getSendListener(null);

            assert actualListener == null;
        }
    }

    @Nested
    class GetSendListenerTests {
        @Test
        void shouldReturnListenerForEventType() throws ModelProviderException {
            for (EventType eventType : EventType.values()) {
                MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
                notifier.setSendListener(eventType, listener);
                MailCompleteListener actualListener = notifier.getSendListener(eventType);

                assert actualListener == listener;
            }
        }

        @Test
        void shouldReturnNullForNonExistentEventType() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            MailCompleteListener actualListener = notifier.getSendListener(null);

            assert actualListener == null;
        }
    }

    @Nested
    class RemoveSendListenerTests {
        @Test
        void shouldRemoveListenerForEventType() throws ModelProviderException {
            for (EventType eventType : EventType.values()) {
                MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
                notifier.setSendListener(eventType, listener);
                notifier.removeSendListener(eventType);
                MailCompleteListener actualListener = notifier.getSendListener(eventType);

                assert actualListener == null;
            }
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void shouldNotRemoveListenerForNonExistentEventType() throws ModelProviderException {
            MailNotifier notifier = new MailNotifier(transaction, clientProvider, (_, _, _) -> {});
            notifier.removeSendListener(null);

            for (EventType eventType : EventType.values()) {
                MailCompleteListener actualListener = notifier.getSendListener(eventType);
                assert actualListener == null;
            }
        }
    }

    @Nested
    class DefaultClientProviderTests {
        @Test
        void shouldCreateDefaultClientProvider() {
            ClientDao clientDao = mock(ClientDao.class);
            ClientProvider provider = new DefaultClientProvider(clientDao);
            Assertions.assertNotNull(provider);
        }

        @Test
        void shouldReturnClientFromClientDao() throws ModelProviderException, SQLException {
            ClientDao clientDao = mock(ClientDao.class);
            ClientProvider provider = new DefaultClientProvider(clientDao);
            when(clientDao.getClient(any())).thenReturn(client);
            Client actualClient = provider.getClient("1234567890");
            assert actualClient == client;
        }

        @Test
        void shouldThrowModelProviderExceptionOnSQLException() throws SQLException {
            ClientDao clientDao = mock(ClientDao.class);
            ClientProvider provider = new DefaultClientProvider(clientDao);
            when(clientDao.getClient(any())).thenThrow(SQLException.class);
            Assertions.assertThrows(ModelProviderException.class, () -> provider.getClient("1234567890"));
        }
    }
}