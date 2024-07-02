package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.ClientDao;
import org.example.dao.EnvoyerDao;
import org.example.dao.FraisDao;
import org.example.dao.TauxDao;
import org.example.models.Client;
import org.example.models.Envoyer;
import org.example.util.exceptions.ModelProviderException;
import org.example.util.interfaces.ObservableMailNotifier;
import org.example.util.mail.MailNotifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/envoyer")
public class EnvoyerServlet extends HttpServlet {

    private EnvoyerDao envoyerDAO;
    private ClientDao clientDao;

    @Override
    public void init() throws ServletException {
        super.init();
        envoyerDAO = new EnvoyerDao();
        clientDao = new ClientDao(); // Assuming you have a ClientDao for fetching clients
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            if (action.equals("list")) {
                listEnvoyer(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(@NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "insert" -> insertEnvoyer(request, response);
                case "update" -> updateEnvoyer(request, response);
                case "delete" -> deleteEnvoyer(request, response);
                default -> response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException | ModelProviderException e) {
            throw new ServletException(e);
        }
    }

    private void listEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Envoyer> listEnvoyer = envoyerDAO.listAllEnvois();
        List<Client> listClients = clientDao.listAllClients(); // Fetching list of clients
        request.setAttribute("listEnvoyer", listEnvoyer);
        request.setAttribute("listClients", listClients); // Adding list of clients to request attributes
        request.setAttribute("recette", getRecette());
        request.getRequestDispatcher("/envoyer.jsp").forward(request, response);
    }

    private void insertEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ModelProviderException {
        int idEnv = 0; // Fake idEnv
        Envoyer newEnvoyer = getEnvoyerFromRequestParams(request, idEnv);

        // Fetching client objects
        Client envoyeur = clientDao.getClient(newEnvoyer.numEnvoyeur());
        Client recepteur = clientDao.getClient(newEnvoyer.numRecepteur());

        if (envoyeur == null || recepteur == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid clients");
            return;
        }

        envoyerDAO.insertEnvoyer(newEnvoyer);

        // Updating solde of envoyeur and recepteur
        float fraisValueForMontant = new FraisDao().getFraisValueForMontant(newEnvoyer.montant(), envoyeur.pays());
        float newEnvoyeurSolde = envoyeur.solde() - newEnvoyer.montant() - fraisValueForMontant;
        float newRecepteurSolde = recepteur.solde() + new TauxDao().convert(envoyeur.pays(), recepteur.pays(), newEnvoyer.montant());
        clientDao.updateSolde(newEnvoyer.numEnvoyeur(), newEnvoyeurSolde);
        clientDao.updateSolde(newEnvoyer.numRecepteur(), newRecepteurSolde);

        // Send mail to récepteur and envoyeur and log success/failure
        MailNotifier mailNotifier = new MailNotifier(newEnvoyer);
        mailNotifier.setSendListener(
                ObservableMailNotifier.EventType.SEND_SUCCESS,
                (message) -> Logger.getLogger(getClass().getName()).info("Mail sent to recepteur: " + message)
        );
        mailNotifier.setSendListener(
                ObservableMailNotifier.EventType.SEND_FAILURE,
                (message) -> Logger.getLogger(getClass().getName()).warning("Failed to send mail to recepteur: " + message)
        );
        Thread _ = mailNotifier.notify(MailNotifier.Party.CASH_RECEIVER);
        Thread _ = mailNotifier.notify(MailNotifier.Party.CASH_SENDER);

        response.sendRedirect("envoyer");
    }

    private void updateEnvoyer(@NotNull HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idenv"));
        Envoyer envoyer = getEnvoyerFromRequestParams(request, idEnv);

        // Fetching client objects
        Client envoyeur = clientDao.getClient(request.getParameter("numEnvoyeur"));
        Client recepteur = clientDao.getClient(request.getParameter("numRecepteur"));

        if (envoyeur == null || recepteur == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid clients");
            return;
        }

        // Checking envoyeur's solde
        Envoyer oldEnvoyer = envoyerDAO.getEnvoyer(idEnv);
        float oldMontant = oldEnvoyer.montant();
        float diffMontant = envoyer.montant() - oldMontant;
        envoyerDAO.updateEnvoyer(envoyer);

        // Adjusting solde of envoyeur and recepteur
        float newEnvoyeurSolde = envoyeur.solde() - diffMontant;
        float newRecepteurSolde = recepteur.solde() + diffMontant;
        clientDao.updateSolde(envoyer.numEnvoyeur(), newEnvoyeurSolde);
        clientDao.updateSolde(envoyer.numRecepteur(), newRecepteurSolde);

        response.sendRedirect("envoyer");
    }

    private static @NotNull Envoyer getEnvoyerFromRequestParams(@NotNull HttpServletRequest request, int idEnv) {
        Date date = new Date();
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        float montant = Float.parseFloat(request.getParameter("montant"));
        String raison = request.getParameter("raison");
        return new Envoyer(idEnv, numEnvoyeur, numRecepteur, montant, date, raison, 0);
    }

    private void deleteEnvoyer(@NotNull HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idenv"));
        Envoyer envoyer = envoyerDAO.getEnvoyer(idEnv);
        if (envoyer == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid envoyer ID");
            return;
        }

        // Deleting Envoyer
        envoyerDAO.deleteEnvoyer(idEnv);
        response.sendRedirect("envoyer");
    }

    private float getRecette() throws SQLException {
        List<Envoyer> listEnvoyer = envoyerDAO.listAllEnvois();
        float recette = 0;
        for (Envoyer envoyer : listEnvoyer) {
            String pays = clientDao.getClient(envoyer.numEnvoyeur()).pays();
            recette += new TauxDao().convert(pays, "madagascar", envoyer.frais());
        }
        return recette;
    }
}
