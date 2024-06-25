package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.EnvoyerDao;
import org.example.models.Envoyer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EnvoyerServlet {

    private EnvoyerDao envoyerDAO;


    public void init() {
        envoyerDAO = new EnvoyerDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "insert":
                    insertEnvoyer(request, response);
                    break;
                case "delete":
                    deleteEnvoyer(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateEnvoyer(request, response);
                    break;
                case "list":
                default:
                    listEnvoyer(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        List<Envoyer> listEnvoyer = envoyerDAO.listAllEnvois();
        request.setAttribute("listEnvoyer", listEnvoyer);
        request.getRequestDispatcher("/envoyer/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/envoyer/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idEnv"));
        Envoyer existingEnvoyer = envoyerDAO.getEnvoyer(idEnv);
        request.setAttribute("envoyer", existingEnvoyer);
        request.getRequestDispatcher("/envoyer/form.jsp").forward(request, response);
    }

    private void insertEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        Date date = new Date();
        String raison = request.getParameter("raison");

        Envoyer newEnvoyer = new Envoyer(numEnvoyeur, numRecepteur, montant, date, raison);
        envoyerDAO.insertEnvoyer(newEnvoyer);
        response.sendRedirect("envoyer");
    }

    private void updateEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idEnv"));
        String numEnvoyeur = request.getParameter("numEnvoyeur");
        String numRecepteur = request.getParameter("numRecepteur");
        int montant = Integer.parseInt(request.getParameter("montant"));
        String raison = request.getParameter("raison");

        Envoyer envoyer = new Envoyer(numEnvoyeur, numRecepteur, montant, new Date(), raison);
        envoyerDAO.updateEnvoyer(envoyer);
        response.sendRedirect("envoyer");
    }

    private void deleteEnvoyer(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        int idEnv = Integer.parseInt(request.getParameter("idEnv"));
        envoyerDAO.deleteEnvoyer(idEnv);
        response.sendRedirect("envoyer");
    }
}
