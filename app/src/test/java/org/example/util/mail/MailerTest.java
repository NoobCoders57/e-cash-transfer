package org.example.util.mail;

import org.example.util.interfaces.SessionProvider;
import org.example.util.interfaces.TransportProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class MailerTest {
    @Mock
    private Session session;

    @Mock
    private TransportProvider transportProvider;

    @Mock
    private SessionProvider sessionProvider;

    private AutoCloseable mocks;

    @BeforeEach
    public void setup() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Nested
    class SendMethodTests {
        @Test
        public void shouldSucceedWhenGivenValidParameters() throws MessagingException {
            doNothing().when(transportProvider).send(any(MimeMessage.class));
            when(sessionProvider.getSession(any(Properties.class), any(Authenticator.class))).thenReturn(session);

            new Mailer(
                    transportProvider, sessionProvider
            ).send(
                    "subject", "message", "recipient@example.com"
            );

            verify(transportProvider, times(1)).send(any(MimeMessage.class));
        }

        @Test
        public void shouldThrowMessagingExceptionWhenTransportFails() throws MessagingException {
            doThrow(MessagingException.class).when(transportProvider).send(any(MimeMessage.class));
            when(sessionProvider.getSession(any(Properties.class), any(Authenticator.class))).thenReturn(session);

            assertThrows(
                    MessagingException.class,
                    () -> new Mailer(
                            transportProvider, sessionProvider
                    ).send(
                            "subject", "message", "recipient@example.com"
                    )
            );
        }
    }
}