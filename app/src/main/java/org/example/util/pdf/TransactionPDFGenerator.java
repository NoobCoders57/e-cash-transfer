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
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionPDFGenerator implements ReleveOperationWriter {
    public final String MONETARY_UNIT = "Ariary";

    private final EnvoyerProvider envoyerProvider;
    private final ClientProvider clientProvider;

    public TransactionPDFGenerator(EnvoyerProvider envoyerProvider, ClientProvider clientProvider) {
        this.envoyerProvider = envoyerProvider;
        this.clientProvider = clientProvider;
    }

    public TransactionPDFGenerator() {
        this(new DefaultEnvoyerProvider(new EnvoyerDao()), new DefaultClientProvider(new ClientDao()));
    }

    @Override
    public void writeReleveOperation(@NotNull Client client, @NotNull YearMonth month, @NotNull OutputStream out) throws ModelProviderException {
        Date dateStart = Date.from(month.atDay(1).atStartOfDay().toInstant((ZoneOffset) ZoneOffset.systemDefault()));
        Date dateEnd = Date.from(month.atEndOfMonth().atStartOfDay().toInstant((ZoneOffset) ZoneOffset.systemDefault()));
        List<Envoyer> transactions = envoyerProvider.allTransactions(client.numtel(), dateStart, dateEnd);
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

        for (Envoyer transaction : transactions) {
            table.addCell(formatter.format(transaction.date().toInstant()));
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
