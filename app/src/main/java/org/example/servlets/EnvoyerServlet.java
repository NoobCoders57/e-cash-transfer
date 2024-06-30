package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.ClientDao;
import org.example.dao.EnvoyerDao;
import org.example.dao.FraisDao;
import org.example.models.Client;
import org.example.models.Envoyer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        } catch (SQLException e) {
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

    private void insertEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
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
        int fraisValueForMontant = new FraisDao().getFraisValueForMontant(newEnvoyer.montant());
        int newEnvoyeurSolde = envoyeur.solde() - newEnvoyer.montant() - fraisValueForMontant;
        int newRecepteurSolde = recepteur.solde() + newEnvoyer.montant();
        clientDao.updateSolde(newEnvoyer.numEnvoyeur(), newEnvoyeurSolde);
        clientDao.updateSolde(newEnvoyer.numRecepteur(), newRecepteurSolde);

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
        int oldMontant = oldEnvoyer.montant();
        int diffMontant = envoyer.montant() - oldMontant;
        envoyerDAO.updateEnvoyer(envoyer);

        // Adjusting solde of envoyeur and recepteur
        int newEnvoyeurSolde = envoyeur.solde() - diffMontant;
        int newRecepteurSolde = recepteur.solde() + diffMontant;
        clientDao.updateSolde(envoyer.numEnvoyeur(), newEnvoyeurSolde);
        clientDao.updateSolde(envoyer.numRecepteur(), newRecepteurSolde);

        response.sendRedirect("envoyer");
    }

    private static @NotNull Envoyer getEnvoyerFromRequestParams(HttpServletRequest request, int idEnv) {
        Date date = new Date();
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        String raison = request.getParameter("raison");
        return new Envoyer(idEnv, numEnvoyeur, numRecepteur, montant, date, raison);
    }

    private void deleteEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idenv"));
        Envoyer envoyer = envoyerDAO.getEnvoyer(idEnv);
        if (envoyer == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid envoyer ID");
            return;
        }

        // Fetching client objects
        Client envoyeur = clientDao.getClient(envoyer.numEnvoyeur());
        Client recepteur = clientDao.getClient(envoyer.numRecepteur());

        if (envoyeur == null || recepteur == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid clients");
            return;
        }

        // Adjusting solde of envoyeur and recepteur
        int montant = envoyer.montant();
        int newEnvoyeurSolde = envoyeur.solde() + montant; // Reverting envoyeur's solde
        int newRecepteurSolde = recepteur.solde() - montant; // Reverting recepteur's solde
        clientDao.updateSolde(envoyeur.numtel(), newEnvoyeurSolde);
        clientDao.updateSolde(recepteur.numtel(), newRecepteurSolde);

        // Deleting Envoyer
        envoyerDAO.deleteEnvoyer(idEnv);
        response.sendRedirect("envoyer");
    }

    private int getRecette() throws SQLException {
        List<Envoyer> listEnvoyer = envoyerDAO.listAllEnvois();
        FraisDao fraisDao = new FraisDao();
        int recette = 0;
        for (Envoyer envoyer : listEnvoyer) {
            recette += fraisDao.getFraisValueForMontant(envoyer.montant());
        }
        return recette;
    }
}
