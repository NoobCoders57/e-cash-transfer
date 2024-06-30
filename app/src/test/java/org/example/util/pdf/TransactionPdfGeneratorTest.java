package org.example.util.pdf;

import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.ClientProvider;
import org.example.util.interfaces.EnvoyerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.OutputStream;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionPdfGeneratorTest {
    private TransactionPdfGenerator generator;
    private AutoCloseable mocks;
    private YearMonth month;
    @Mock private EnvoyerProvider envoyerProvider;
    @Mock private ClientProvider clientProvider;
    @Mock private Envoyer envoyer;
    @Mock private Client client;
    @Mock private OutputStream out;

    @BeforeEach
    void setUp() throws IOException, ModelProviderException {
        mocks = MockitoAnnotations.openMocks(this);

        doNothing().when(out).write(any());
        doNothing().when(out).write(any(byte[].class), anyInt(), anyInt());

        when(clientProvider.getClient(any())).thenReturn(client);

        when(client.nom()).thenReturn("John Doe");
        when(client.numtel()).thenReturn("123456789");
        when(client.sexe()).thenReturn("Masculin");
        when(client.solde()).thenReturn(1000);

        when(envoyer.numRecepteur()).thenReturn("987654321");
        when(envoyer.montant()).thenReturn(500);
        when(envoyer.date()).thenReturn(new Date());
        when(envoyer.raison()).thenReturn("Test");

        month = YearMonth.now();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Nested
    class WriteReleveOperationTests {

        @Test
        void testWithValidInputs() throws IOException, ModelProviderException {
            when(envoyerProvider.sentTransactions(any(), any(), any())).thenReturn(List.of(envoyer));
            generator = new TransactionPdfGenerator(envoyerProvider, clientProvider);
            assertDoesNotThrow(() -> generator.writeReleveOperation(client, month, out));
            verify(envoyerProvider, atLeastOnce()).sentTransactions(any(), any(), any());
            verify(out, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
        }

        @Test
        void testWithNoTransactions() throws IOException, ModelProviderException {
            when(envoyerProvider.sentTransactions(any(), any(), any())).thenReturn(List.of());
            generator = new TransactionPdfGenerator(envoyerProvider, clientProvider);
            assertDoesNotThrow(() -> generator.writeReleveOperation(client, month, out));
            verify(out, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
            verify(clientProvider, never()).getClient(any());
        }

        @Test
        void testWithMultipleTransactions() throws ModelProviderException {
            when(envoyerProvider.sentTransactions(any(), any(), any())).thenReturn(List.of(envoyer, envoyer));
            generator = new TransactionPdfGenerator(envoyerProvider, clientProvider);
            assertDoesNotThrow(() -> generator.writeReleveOperation(client, month, out));
            verify(envoyerProvider, atLeastOnce()).sentTransactions(any(), any(), any());
            verify(envoyer, atLeast(2)).numRecepteur();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void testWithNullClient() {
            generator = new TransactionPdfGenerator(envoyerProvider, clientProvider);
            assertThrows(NullPointerException.class, () -> generator.writeReleveOperation(null, month, out));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void testWithNullMonth() {
            generator = new TransactionPdfGenerator(envoyerProvider, clientProvider);
            assertThrows(NullPointerException.class, () -> generator.writeReleveOperation(client, null, out));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void testWithNullOutputStream() {
            generator = new TransactionPdfGenerator(envoyerProvider, clientProvider);
            assertThrows(NullPointerException.class, () -> generator.writeReleveOperation(client, month, null));
        }
    }
}