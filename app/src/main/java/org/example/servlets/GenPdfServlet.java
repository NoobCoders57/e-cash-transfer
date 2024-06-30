package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.ReleveOperationWriter;
import org.example.util.pdf.TransactionPdfGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

@WebServlet("/gen-pdf")
public class GenPdfServlet extends HttpServlet {
    private ReleveOperationWriter releveOperationWriter;

    @Override
    public void init() throws ServletException {
        super.init();
        releveOperationWriter = new TransactionPdfGenerator();
    }

    @Override
    protected void doGet(@NotNull HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String numClient = req.getParameter("client");
        String monthString = req.getParameter("month");
        String yearString = req.getParameter("year");
        if (Stream.of(numClient, monthString, yearString).anyMatch(Objects::isNull)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameter, please provide client, month, and year");
            return;
        }

        int year, month;
        try {
            month = Integer.parseInt(monthString);
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).warning("Invalid month: " + monthString);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid month: " + monthString);
            return;
        }
        try {
            year = Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            Logger.getLogger(getClass().getName()).warning("Invalid year: " + yearString);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid year: " + yearString);
            return;
        }

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=client-" + numClient + ".pdf");

        try {
            releveOperationWriter.writeReleveOperation(numClient, YearMonth.of(year, month), resp.getOutputStream());
        } catch (ModelProviderException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate PDF: " + e.getMessage());
        }
    }
}
