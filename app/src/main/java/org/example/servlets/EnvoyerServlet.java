package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.ClientDao;
import org.example.dao.EnvoyerDao;
import org.example.models.Client;
import org.example.models.Envoyer;

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
            switch (action) {
                case "list":
                    listEnvoyer(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    break;
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
                case "insert":
                    insertEnvoyer(request, response);
                    break;
                case "update":
                    updateEnvoyer(request, response);
                    break;
                case "delete":
                    deleteEnvoyer(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    break;
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
        request.getRequestDispatcher("/envoyer.jsp").forward(request, response);
    }

    private void insertEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        Date date = new Date();
        String raison = request.getParameter("raison");

        // Fetching client objects
        Client envoyeur = clientDao.getClient(numEnvoyeur);
        Client recepteur = clientDao.getClient(numRecepteur);

        if (envoyeur == null || recepteur == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid clients");
            return;
        }

        // Creating new Envoyer object
        Envoyer newEnvoyer = new Envoyer(numEnvoyeur, numRecepteur, montant, date, raison);
        envoyerDAO.insertEnvoyer(newEnvoyer);

        // Updating solde of envoyeur and recepteur
        int newEnvoyeurSolde = envoyeur.solde() - montant;
        int newRecepteurSolde = recepteur.solde() + montant;
        clientDao.updateSolde(numEnvoyeur, newEnvoyeurSolde);
        clientDao.updateSolde(numRecepteur, newRecepteurSolde);

        response.sendRedirect("envoyer");
    }

    private void updateEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idEnv"));
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        String raison = request.getParameter("raison");

        // Fetching client objects
        Client envoyeur = clientDao.getClient(numEnvoyeur);
        Client recepteur = clientDao.getClient(numRecepteur);

        if (envoyeur == null || recepteur == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid clients");
            return;
        }

        // Checking envoyeur's solde
        Envoyer oldEnvoyer = envoyerDAO.getEnvoyer(idEnv);
        int oldMontant = oldEnvoyer.montant();
        int diffMontant = montant - oldMontant;

        // Updating Envoyer object
        Envoyer envoyer = new Envoyer(idEnv, numEnvoyeur, numRecepteur, montant, new Date(), raison);
        envoyerDAO.updateEnvoyer(envoyer);

        // Adjusting solde of envoyeur and recepteur
        int newEnvoyeurSolde = envoyeur.solde() - diffMontant;
        int newRecepteurSolde = recepteur.solde() + diffMontant;
        clientDao.updateSolde(numEnvoyeur, newEnvoyeurSolde);
        clientDao.updateSolde(numRecepteur, newRecepteurSolde);

        response.sendRedirect("envoyer");
    }

    private void deleteEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idEnv"));
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
}
