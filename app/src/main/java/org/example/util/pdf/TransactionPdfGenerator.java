package org.example.util.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.example.dao.ClientDao;
import org.example.dao.EnvoyerDao;
import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.ClientProvider;
import org.example.util.interfaces.EnvoyerProvider;
import org.example.util.interfaces.ReleveOperationWriter;
import org.example.util.providers.DefaultClientProvider;
import org.example.util.providers.DefaultEnvoyerProvider;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A class that generates a PDF file containing a client's sent cash transactions for a given month.
 */
public class TransactionPdfGenerator implements ReleveOperationWriter {
    public final String MONETARY_UNIT = "Ariary";

    private final EnvoyerProvider envoyerProvider;
    private final ClientProvider clientProvider;

    /**
     * Constructs a new TransactionPdfGenerator with the given EnvoyerProvider and ClientProvider.
     *
     * @param envoyerProvider the EnvoyerProvider to use
     * @param clientProvider  the ClientProvider to use
     */
    public TransactionPdfGenerator(EnvoyerProvider envoyerProvider, ClientProvider clientProvider) {
        this.envoyerProvider = envoyerProvider;
        this.clientProvider = clientProvider;
    }

    /**
     * Constructs a new TransactionPdfGenerator with the default EnvoyerProvider and ClientProvider.
     */
    public TransactionPdfGenerator() {
        this(new DefaultEnvoyerProvider(new EnvoyerDao()), new DefaultClientProvider(new ClientDao()));
    }

    @Override
    public void writeReleveOperation(@NotNull Client client, @NotNull YearMonth month, @NotNull OutputStream out) throws ModelProviderException {
        ZoneOffset zoneOffset = OffsetDateTime.now(ZoneId.systemDefault()).getOffset();
        Date dateStart = Date.from(month.atDay(1).atStartOfDay().toInstant(zoneOffset));
        Date dateEnd = Date.from(month.atEndOfMonth().atStartOfDay().toInstant(zoneOffset));
        List<Envoyer> transactions = envoyerProvider.sentTransactions(client.numtel(), dateStart, dateEnd);
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));

        Document document = new Document(pdfDocument);

        // Add the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);
        document.add(new Paragraph("Date: " + month.format(formatter)));

        // Add the client's phone number, name, gender, and current balance
        document.add(new Paragraph("Contact : " + client.numtel()));
        document.add(new Paragraph(client.nom()));
        document.add(new Paragraph(client.sexe()));
        document.add(new Paragraph("Solde actuel : %d %s".formatted(client.solde(), MONETARY_UNIT)));

        // Create a table and add the transactions to it
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}));
        table.addHeaderCell("Date");
        table.addHeaderCell("Raison");
        table.addHeaderCell("Nom du récepteur");
        table.addHeaderCell("Montant");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Envoyer transaction : transactions) {
            table.addCell(simpleDateFormat.format(transaction.date()));
            table.addCell(transaction.raison());
            table.addCell(clientProvider.getClient(transaction.numRecepteur()).nom());
            table.addCell(String.valueOf(transaction.montant()));
        }

        document.add(table);

        // Add the total
        double total = transactions.stream().mapToDouble(Envoyer::montant).sum();
        document.add(new Paragraph("Total: " + total));

        document.close();
    }
}
